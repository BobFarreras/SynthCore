package com.deixebledenkaito.synthcore.ui.screen.empresa.registre

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.deixebledenkaito.synthcore.ui.viewmodel.EmpresaViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistreEmpresaScreen(
    viewModel: EmpresaViewModel = hiltViewModel(),
    onSuccess: (String, String) -> Unit  // (empresaId, codiInvitacio)
) {
    val state by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .verticalScroll(scrollState),
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
            onValueChange = { viewModel.onNomChange(it) },
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

        Spacer(modifier = Modifier.height(16.dp))

        // Botó de Registre
        Button(
            onClick = {
                scope.launch {
                    viewModel.registraEmpresa()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isLoading
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp,
                    color = Color.White
                )
            } else {
                Text("Registrar Empresa")
            }
        }

        // Mostrar errors
        state.error?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                textAlign = TextAlign.Center
            )
        }
    }

// Redirecció si tot va bé
    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            state.empresaId?.let { empresaId ->
                state.invitationCode?.let { codi ->
                    onSuccess(empresaId, codi)
                }
            }
        }
    }
}