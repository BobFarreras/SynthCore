package com.deixebledenkaito.synthcore.data.model

// data/model/Treballador.kt
data class Treballador(
    val id: String = "",
    val nom: String = "",
    val email: String = "",
    val rol: String = "empleat",  // "admin" o "empleat"
    val empresaId: String = ""
)