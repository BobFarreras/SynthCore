package com.deixebledenkaito.synthcore.ui.screen.empresa.registre

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch

// ui/screen/empresa/register/EmpresaRegisterScreen.kt
@Composable
fun EmpresaRegisterScreen(
    viewModel: EmpresaRegisterViewModel = hiltViewModel(),
    onRegisterSuccess: (String, String) -> Unit,  // (empresaId, codiInvitacio)
    onNavigateToLogin: () -> Unit
) {
    val state by viewModel.registerState.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(state.isRegisterSuccess) {
        if (state.isRegisterSuccess) {
            state.empresaId?.let { empresaId ->
                state.invitationCode?.let { codi ->
                    onRegisterSuccess(empresaId, codi)
                }
            }
        }
    }

    LaunchedEffect(state.error) {
        state.error?.let { error ->
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
            viewModel.onRegisterErrorConsumed()
        }
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Registre d'Empresa",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )

        // Camp Nom
        OutlinedTextField(
            value = state.nom,
            onValueChange = { viewModel.onRegisterNomChange(it) },
            label = { Text("Nom de l'empresa") },
            modifier = Modifier.fillMaxWidth()
        )

        // Camp Email
        OutlinedTextField(
            value = state.email,
            onValueChange = { viewModel.onEmailChange(it) },
            label = { Text("Email de l'empresa") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )

        // Camp Direcció
        OutlinedTextField(
            value = state.direccio,
            onValueChange = { viewModel.onDireccioChange(it) },
            label = { Text("Direcció") },
            modifier = Modifier.fillMaxWidth()
        )

        // Camp CIF
        OutlinedTextField(
            value = state.cif,
            onValueChange = { viewModel.onCifChange(it) },
            label = { Text("CIF") },
            modifier = Modifier.fillMaxWidth()
        )

        // Camp Password
        OutlinedTextField(
            value = state.password,
            onValueChange = { viewModel.onPasswordChange(it) },
            label = { Text("Contrasenya") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Botó de Registre
        Button(
            onClick = {
                if (state.nom.isBlank() || state.email.isBlank() ||
                    state.direccio.isBlank() || state.cif.isBlank() ||
                    state.password.isBlank()) {
                    Toast.makeText(context, "Si us plau, omple tots els camps", Toast.LENGTH_SHORT).show()
                } else {
                    scope.launch {
                        viewModel.register()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isLoading
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Registrar Empresa")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botó per anar a Login
        Button(
            onClick = onNavigateToLogin,

        ) {
            Text("Ja tens compte? Inicia sessió")
        }
    }
}