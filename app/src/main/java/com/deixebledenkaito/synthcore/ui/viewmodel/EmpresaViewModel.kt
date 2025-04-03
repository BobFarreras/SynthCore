package com.deixebledenkaito.synthcore.ui.viewmodel


import androidx.lifecycle.ViewModel

import com.deixebledenkaito.synthcore.domain.model.Empresa
import com.deixebledenkaito.synthcore.domain.usecase.RegistraEmpresaUseCase
import com.google.firebase.auth.FirebaseAuth

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.tasks.await


import javax.inject.Inject


// ui/viewmodel/EmpresaViewModel.kt
@HiltViewModel
class EmpresaViewModel @Inject constructor(
    private val registraEmpresaUseCase: RegistraEmpresaUseCase,
    private val auth: FirebaseAuth  // Afegim Auth
) : ViewModel() {

    private val _uiState = MutableStateFlow(EmpresaUiState())
    val uiState: StateFlow<EmpresaUiState> = _uiState.asStateFlow()

    fun onNomChange(nom: String) { _uiState.update { it.copy(nom = nom) } }
    fun onEmailChange(email: String) { _uiState.update { it.copy(email = email) } }
    fun onDireccioChange(direccio: String) { _uiState.update { it.copy(direccio = direccio) } }
    fun onCifChange(cif: String) { _uiState.update { it.copy(cif = cif) } }
    fun onPasswordChange(password: String) { _uiState.update { it.copy(password = password) } }

    suspend fun registraEmpresa() {
        _uiState.update { it.copy(isLoading = true, error = null) }

        try {
            // 1. Crear usuari a Auth
            val authResult = auth.createUserWithEmailAndPassword(
                _uiState.value.email,
                _uiState.value.password
            ).await()

            // 2. Crear empresa a Firestore
            val empresa = Empresa(
                id = authResult.user?.uid ?: "",
                nom = _uiState.value.nom,
                email = _uiState.value.email,
                direccio = _uiState.value.direccio,
                cif = _uiState.value.cif
            )

            val result = registraEmpresaUseCase(empresa)

            result.fold(
                onSuccess = {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isSuccess = true,
                            empresaId = empresa.id,
                            invitationCode = empresa.codiInvitacio  // Guardem el codi
                        )
                    }
                },
                onFailure = { error ->
                    // Si falla Firestore, eliminar usuari d'Auth per consistència
                    auth.currentUser?.delete()?.await()
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = error.message
                        )
                    }
                }
            )
        } catch (e: Exception) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }
}

data class EmpresaUiState(
    val nom: String = "",
    val email: String = "",
    val direccio: String = "",
    val cif: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val empresaId: String? = null,
    val invitationCode: String? = null,
    val error: String? = null
)