package com.deixebledenkaito.synthcore.data.repository

import com.deixebledenkaito.synthcore.domain.model.Empresa
import com.deixebledenkaito.synthcore.domain.repository.EmpresaRepository
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

import kotlinx.coroutines.tasks.await

import javax.inject.Inject

// data/repository/EmpresaRepositoryImpl.kt
class EmpresaRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : EmpresaRepository {

    override suspend fun registraEmpresa(empresa: Empresa): Result<String> {
        return try {
            val empresaData = hashMapOf(
                "id" to empresa.id,
                "nom" to empresa.nom,
                "email" to empresa.email,
                "direccio" to empresa.direccio,
                "cif" to empresa.cif,
                "codiInvitacio" to empresa.codiInvitacio,
                "dataRegistre" to FieldValue.serverTimestamp()
            )

            // Utilitzem l'ID de l'empresa com a ID del document
            firestore.collection("empreses").document(empresa.id).set(empresaData).await()
            Result.success(empresa.id)
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