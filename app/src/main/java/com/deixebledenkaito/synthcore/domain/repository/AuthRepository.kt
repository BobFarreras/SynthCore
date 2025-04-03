package com.deixebledenkaito.synthcore.domain.repository

import com.deixebledenkaito.synthcore.domain.model.Empresa
import com.google.firebase.auth.FirebaseUser

// domain/repository/AuthRepository.kt
interface AuthRepository {
    suspend fun loginEmpresa(email: String, password: String): Result<FirebaseUser>
    suspend fun registerEmpresa(email: String, password: String, empresaData: Empresa): Result<Pair<String, String>>
    suspend fun logout()
    fun isUserLogged(): Boolean
    fun getCurrentUserId(): String?
}
