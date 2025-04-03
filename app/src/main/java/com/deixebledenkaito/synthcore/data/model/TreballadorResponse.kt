package com.deixebledenkaito.synthcore.data.model

import com.deixebledenkaito.synthcore.domain.model.Treballador
import com.google.firebase.firestore.PropertyName

data class TreballadorResponse(
    @PropertyName("nom") val nom: String = "",
    @PropertyName("email") val email: String = "",
    @PropertyName("rol") val rol: String = "empleat",  // Valor per defecte
    @PropertyName("empresa_id") val empresaId: String = "",
    @PropertyName("uid") val uid: String = ""  // Relació amb Firebase Auth
) {
    fun toDomain(): com.deixebledenkaito.synthcore.domain.model.Treballador {
        return Treballador(
            id = uid,  // Utilitzem l'UID com a ID
            nom = nom,
            email = email,
            rol = rol,
            empresaId = empresaId
        )
    }
}