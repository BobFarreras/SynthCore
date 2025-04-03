package com.deixebledenkaito.synthcore.data.repository

import android.util.Log
import com.deixebledenkaito.synthcore.domain.model.Empresa
import com.deixebledenkaito.synthcore.domain.repository.AuthRepository
import com.deixebledenkaito.synthcore.domain.repository.EmpresaRepository
import com.google.firebase.auth.AuthResult

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import javax.inject.Singleton
import kotlin.Result



// data/repository/AuthRepositoryImpl.kt
@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val empresaRepository: EmpresaRepository
) : AuthRepository {
    private val TAG = "AuthRepositoryImpl"

    override suspend fun loginEmpresa(email: String, password: String): Result<FirebaseUser> {
        return try {
            Log.d(TAG, "Intentant login per l'empresa amb email: $email")
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val user = result.user ?: throw Exception("No s'ha pogut obtenir l'usuari")

            Log.d(TAG, "Login exitós per l'usuari: ${user.uid}")
            Result.success(user)
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            Log.e(TAG, "Credencials incorrectes", e)
            Result.failure(Exception("Email o contrasenya incorrectes"))
        } catch (e: FirebaseAuthInvalidUserException) {
            Log.e(TAG, "Usuari no trobat", e)
            Result.failure(Exception("No existeix cap empresa amb aquest email"))
        } catch (e: Exception) {
            Log.e(TAG, "Error desconegut en login", e)
            Result.failure(Exception("Error en l'inici de sessió: ${e.message}"))
        }
    }

    override suspend fun registerEmpresa(
        email: String,
        password: String,
        empresaData: Empresa
    ): Result<Pair<String, String>> {
        var authResult: AuthResult? = null // Declarem la variable fora del try per poder-la utilitzar al catch

        return try {
            Log.d(TAG, "Intentant registrar empresa amb email: $email")

            // 1. Crear usuari a Firebase Auth
            authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val userId = authResult.user?.uid ?: throw Exception("No s'ha pogut obtenir l'ID d'usuari")

            // 2. Crear empresa a Firestore
            val empresaCompleta = empresaData.copy(id = userId, email = email)
            val empresaId = empresaRepository.registraEmpresa(empresaCompleta).getOrThrow()

            Log.d(TAG, "Registre exitós. ID: $userId, Codi: ${empresaCompleta.codiInvitacio}")
            Result.success(Pair(userId, empresaCompleta.codiInvitacio))
        } catch (e: FirebaseAuthWeakPasswordException) {
            Log.e(TAG, "Contrasenya feble", e)
            Result.failure(Exception("La contrasenya és massa feble"))
        } catch (e: FirebaseAuthUserCollisionException) {
            Log.e(TAG, "Email ja en ús", e)
            Result.failure(Exception("Ja existeix una empresa amb aquest email"))
        } catch (e: Exception) {
            Log.e(TAG, "Error en registre", e)
            // Si falla Firestore, eliminar usuari d'Auth per consistència
            try {
                authResult?.user?.delete()?.await()
            } catch (deleteError: Exception) {
                Log.e(TAG, "Error en eliminar usuari després de fallar el registre", deleteError)
            }
            Result.failure(Exception("Error en el registre: ${e.message}"))
        }
    }

    override suspend fun logout() {
        try {
            Log.d(TAG, "Tancant sessió")
            firebaseAuth.signOut()
        } catch (e: Exception) {
            Log.e(TAG, "Error en logout", e)
            throw e
        }
    }

    override fun isUserLogged(): Boolean {
        return firebaseAuth.currentUser != null
    }

    override fun getCurrentUserId(): String? {
        return firebaseAuth.currentUser?.uid
    }
}