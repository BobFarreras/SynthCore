package com.deixebledenkaito.synthcore.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.deixebledenkaito.synthcore.ui.screen.empresa.RegistreEmpresaScreen
import com.deixebledenkaito.synthcore.ui.screen.treballador.RegistreTreballadorScreen


// ui/SynthCoreApp.kt
@Composable
fun SynthCoreApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "registreEmpresa") {
        composable("registreEmpresa") {
            RegistreEmpresaScreen { codiInvitacio ->
                navController.navigate("registreTreballador/$codiInvitacio")
            }
        }
        composable("registreTreballador/{codi}") { backStackEntry ->
            val codi = backStackEntry.arguments?.getString("codi") ?: ""
            RegistreTreballadorScreen(codiInvitacio = codi)
        }
    }
}