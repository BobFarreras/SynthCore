package com.deixebledenkaito.synthcore.ui.screen.empresa

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

import kotlinx.coroutines.launch

// ui/screen/empresa/home/IniciEmpresaScreen.kt
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IniciEmpresaScreen(
    empresaId: String,
    invitationCode: String,
    iniciEmpresaViewModel: IniciEmpresaViewModel = hiltViewModel(),
    onLogout: () -> Unit
) {

    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Àrea d'Empresa") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                actions = {
                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                iniciEmpresaViewModel.logout()
                                onLogout()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Logout,
                            contentDescription = "Tancar sessió",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Business,
                contentDescription = "Empresa",
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "Benvingut/da a l'àrea d'empresa",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text("ID de la teva empresa: $empresaId")

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Codi d'invitació pels teus treballadors:",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = invitationCode,
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier
                    .padding(16.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Fes arribar aquest codi als teus treballadors",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}