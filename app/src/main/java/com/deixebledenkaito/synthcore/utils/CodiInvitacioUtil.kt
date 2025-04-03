package com.deixebledenkaito.synthcore.utils


// Utils.kt
fun generateInvitationCode(): String {
    val chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789" // Eliminem caràcters confusos
    return (1..6).map { chars.random() }.joinToString("")
}