package com.deixebledenkaito.synthcore.domain.model



import com.deixebledenkaito.synthcore.utils.generateInvitationCode
import java.util.Date

// domain/model/Empresa.kt
data class Empresa(
    val id: String = "",  // Aquest serà el mateix ID que a Auth
    val nom: String,
    val email: String,    // Aquest serà el mateix que a Auth
    val direccio: String,
    val cif: String,
    val codiInvitacio: String = generateInvitationCode(),  // Nou codi únic
    val dataRegistre: Date? = null
)