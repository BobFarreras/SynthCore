package com.deixebledenkaito.synthcore.ui

import android.annotation.SuppressLint

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.CircularProgressIndicator

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.deixebledenkaito.synthcore.ui.screen.empresa.IniciEmpresaScreen
import com.deixebledenkaito.synthcore.ui.screen.empresa.login.EmpresaLoginScreen
import com.deixebledenkaito.synthcore.ui.screen.empresa.registre.EmpresaRegisterScreen
import com.deixebledenkaito.synthcore.ui.screen.empresa.registre.EmpresaRegisterViewModel

import com.deixebledenkaito.synthcore.ui.screen.treballador.RegistreTreballadorScreen


import kotlinx.coroutines.flow.update


// ui/SynthCoreApp.kt
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun SynthCoreApp() {
    val navController = rememberNavController()
    val authViewModel: EmpresaRegisterViewModel = hiltViewModel()

    // Estats d'autenticació
    val authState by authViewModel.loginState.collectAsState()
    val isCheckingAuth = remember { mutableStateOf(true) }
    val startDestination = remember { mutableStateOf("home") }

    LaunchedEffect(Unit) {
        val isLogged = authViewModel.authRepository.isUserLogged()
        if (isLogged) {
            authViewModel._loginState.update { it.copy(isLoginSuccess = true) }
            startDestination.value = "iniciEmpresa/${authViewModel.authRepository.getCurrentUserId()}/CODIGO_INVITACION"
        }
        isCheckingAuth.value = false
    }

    if (isCheckingAuth.value) {
        Box(
            modifier = Modifier
                .fillMaxSize(),

            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                color = Color.White,
                strokeWidth = 4.dp
            )
        }
        return
    }

    // Navegació normal un cop verificat l'estat
    LaunchedEffect(authState.isLoginSuccess) {
        if (authState.isLoginSuccess) {
            val currentUser = authViewModel.authRepository.getCurrentUserId()
            if (currentUser != null) {
                navController.navigate("iniciEmpresa/$currentUser/CODIGO_INVITACION") {
                    popUpTo("home") { inclusive = true }
                }
            }
        }
    }

    // Resta del codi NavHost...
    NavHost(
        navController = navController,
        startDestination  = startDestination.value
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
                        popUpTo(0) // Això neteja tot l'stack de navegació
                        launchSingleTop = true
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
