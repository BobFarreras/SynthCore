package com.deixebledenkaito.synthcore.ui.screen.empresa

import androidx.compose.foundation.content.MediaType.Companion.Text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.deixebledenkaito.synthcore.ui.viewmodel.EmpresaViewModel
import kotlinx.coroutines.launch

// ui/screen/empresa/RegistreEmpresaScreen.kt
@Composable
fun RegistreEmpresaScreen(
    viewModel: EmpresaViewModel = hiltViewModel(),
    onSuccess: (String) -> Unit  // Navegació a la següent pantalla
) {
    val state = viewModel.state
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = state.nom,
            onValueChange = viewModel::onNomChange,
            label = { Text("Nom de l'empresa") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                scope.launch {
                    viewModel.registraEmpresa()
                    if (state.isSuccess) {
                        onSuccess(state.codiInvitacio)
                    }
                }
            }
        ) {
            Text("Registrar Empresa")
        }


    }
}