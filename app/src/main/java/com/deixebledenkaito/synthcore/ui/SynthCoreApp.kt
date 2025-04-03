package com.deixebledenkaito.synthcore.ui

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.deixebledenkaito.synthcore.ui.screen.empresa.IniciEmpresaScreen
import com.deixebledenkaito.synthcore.ui.screen.empresa.login.EmpresaLoginScreen
import com.deixebledenkaito.synthcore.ui.screen.empresa.registre.EmpresaRegisterScreen

import com.deixebledenkaito.synthcore.ui.screen.treballador.RegistreTreballadorScreen
import com.deixebledenkaito.synthcore.ui.viewmodel.EmpresaAuthViewModel



// ui/SynthCoreApp.kt
@SuppressLint("StateFlowValueCalledInComposition")

@Composable
fun SynthCoreApp() {
    val navController = rememberNavController()
    val authViewModel: EmpresaAuthViewModel = hiltViewModel()

    // Observem l'estat d'autenticació
    LaunchedEffect(authViewModel.loginState.value.isLoginSuccess) {
        if (authViewModel.loginState.value.isLoginSuccess) {
            // Si l'usuari està loguejat, anem directament a la pantalla d'empresa
            val currentUser = authViewModel.authRepository.getCurrentUserId()
            if (currentUser != null) {
                // Aquí hauríem de recuperar el codi d'invitació de la base de dades
                // Per simplificar, ho deixem buit
                navController.navigate("iniciEmpresa/$currentUser/CODIGO_INVITACION") {
                    popUpTo("home") { inclusive = true }
                }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(
                onNavigateToEmpresa = { navController.navigate("empresaAuth") },
                onNavigateToTreballador = { navController.navigate("registreTreballador") }
            )
        }

        // Pantalla d'autenticació (login/registre)
        composable("empresaAuth") {
            EmpresaAuthScreen(
                onLoginSuccess = {
                    navController.navigate("iniciEmpresa/${authViewModel.authRepository.getCurrentUserId()}/CODIGO_INVITACION") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                onRegisterSuccess = { empresaId, codi ->
                    navController.navigate("iniciEmpresa/$empresaId/$codi") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }

        composable("iniciEmpresa/{empresaId}/{invitationCode}") { backStackEntry ->
            val empresaId = backStackEntry.arguments?.getString("empresaId") ?: ""
            val invitationCode = backStackEntry.arguments?.getString("invitationCode") ?: ""

            IniciEmpresaScreen(
                empresaId = empresaId,
                invitationCode = invitationCode,
                onLogout = {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }

        composable("registreTreballador/{codi}") { backStackEntry ->
            val codi = backStackEntry.arguments?.getString("codi") ?: ""
            RegistreTreballadorScreen(codiInvitacio = codi)
        }
    }
}

// Pantalla d'autenticació (conté login i registre)
@Composable
fun EmpresaAuthScreen(
    onLoginSuccess: () -> Unit,
    onRegisterSuccess: (String, String) -> Unit
) {
    var showLogin by remember { mutableStateOf(true) }

    if (showLogin) {
        EmpresaLoginScreen(
            onLoginSuccess = onLoginSuccess,
            onNavigateToRegister = { showLogin = false }
        )
    } else {
        EmpresaRegisterScreen(
            onRegisterSuccess = onRegisterSuccess,
            onNavigateToLogin = { showLogin = true }
        )
    }
}