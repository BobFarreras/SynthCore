package com.deixebledenkaito.synthcore.data.model

import com.google.firebase.Timestamp

// data/model/Empresa.kt
data class Empresa(
    val id: String = "",
    val nom: String = "",
    val cif: String = "",
    val codiInvitacio: String = "",
    val dataRegistre: Timestamp = Timestamp.now()
)