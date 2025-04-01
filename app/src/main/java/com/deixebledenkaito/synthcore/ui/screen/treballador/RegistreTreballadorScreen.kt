package com.deixebledenkaito.synthcore.ui.screen.treballador

import androidx.compose.foundation.layout.*

//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.*
import androidx.compose.runtime.*

import androidx.compose.ui.Modifier

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.deixebledenkaito.synthcore.ui.viewmodel.TreballadorViewModel
import kotlinx.coroutines.launch

// ui/screen/treballador/RegistreTreballadorScreen.kt
@Composable
fun RegistreTreballadorScreen(
    codiInvitacio: String,
    viewModel: TreballadorViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Codi d'invitació: $codiInvitacio", fontWeight = FontWeight.Bold)

        TextField(
            value = state.email,
            onValueChange = { viewModel.onEmailChange(it) },
            label = { Text("Email") }
        )

        Button(
            onClick = {
                scope.launch {
                    viewModel.registraTreballador(codiInvitacio)
                }
            }
        ) {
            Text("Registrar-se")
        }
    }
}