// data/model/EmpresaResponse.kt
package com.deixebledenkaito.synthcore.data.model

import com.deixebledenkaito.synthcore.domain.model.Empresa
import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName


data class EmpresaResponse(
    @PropertyName("id") val id: String = "",
    @PropertyName("nom") val nom: String = "",
    @PropertyName("email") val email: String = "",
    @PropertyName("direccio") val direccio: String = "",
    @PropertyName("cif") val cif: String = "",
    // Important: No guardis contrasenyes en plain text a producció
    @PropertyName("codi_invitacio") val codiInvitacio: String = "",
    @PropertyName("data_registre") val dataRegistre: Timestamp? = null
) {
    fun toDomain(): Empresa {
        return Empresa(
            id = id,
            nom = nom,
            email = email,
            direccio = direccio,
            cif = cif,
            codiInvitacio = codiInvitacio,
            dataRegistre = dataRegistre?.toDate()
        )
    }
}