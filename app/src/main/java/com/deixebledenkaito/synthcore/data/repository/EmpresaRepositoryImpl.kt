package com.deixebledenkaito.synthcore.data.repository

import com.deixebledenkaito.synthcore.data.model.Empresa
import com.deixebledenkaito.synthcore.domain.repository.EmpresaRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

// data/repository/EmpresaRepositoryImpl.kt
class EmpresaRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : EmpresaRepository {


    override suspend fun registraEmpresa(empresa: Empresa): Result<String> {
        return try {
            val docRef = firestore.collection("empreses").add(empresa).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun obtenirEmpresaPerCodi(codi: String): Result<Empresa> {
        return try {
            val query = firestore.collection("empreses")
                .whereEqualTo("codiInvitacio", codi)
                .get()
                .await()

            if (query.isEmpty) {
                Result.failure(Exception("Codi invàlid"))
            } else {
                val empresa = query.documents[0].toObject(Empresa::class.java)!!
                Result.success(empresa.copy(id = query.documents[0].id))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}