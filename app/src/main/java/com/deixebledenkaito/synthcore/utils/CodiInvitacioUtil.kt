package com.deixebledenkaito.synthcore.utils


object CodiInvitacioUtil {
    fun generarCodi(): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        return (1..6)
            .map { chars.random() }
            .joinToString("")
    }
}