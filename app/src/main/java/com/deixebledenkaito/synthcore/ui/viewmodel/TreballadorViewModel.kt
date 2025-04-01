package com.deixebledenkaito.synthcore.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

import com.deixebledenkaito.synthcore.domain.repository.EmpresaRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

// ui/viewmodel/TreballadorViewModel.kt
@HiltViewModel
class TreballadorViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val empresaRepository: EmpresaRepository
) : ViewModel() {

    // Estat complet del formulari
    var state by mutableStateOf(TreballadorState())
        private set

    // Funcions per a actualitzar l'estat
    fun onNomChange(nom: String) {
        state = state.copy(nom = nom)
    }

    fun onEmailChange(email: String) {
        state = state.copy(email = email)
    }

    fun onPasswordChange(password: String) {
        state = state.copy(password = password)
    }

    suspend fun registraTreballador(codiInvitacio: String) {
        val empresaResult = empresaRepository.obtenirEmpresaPerCodi(codiInvitacio)

        empresaResult.fold(
            onSuccess = { empresa ->
                try {
                    auth.createUserWithEmailAndPassword(state.email, state.password).await()
                    // Guardar dades addicionals a Firestore aquí si cal
                    state = state.copy(isSuccess = true, error = null)
                } catch (e: Exception) {
                    state = state.copy(error = "Error en el registre: ${e.message}")
                }
            },
            onFailure = { error ->
                state = state.copy(error = "Codi d'invitació invàlid")
            }
        )
    }

    fun isFormValid(): Boolean {
        return state.email.isNotEmpty() &&
                state.password.length >= 6 &&
                state.nom.isNotEmpty()
    }
}

// Estat del formulari de treballador
data class TreballadorState(
    val nom: String = "",
    val email: String = "",
    val password: String = "",
    val isSuccess: Boolean = false,
    val error: String? = null
)