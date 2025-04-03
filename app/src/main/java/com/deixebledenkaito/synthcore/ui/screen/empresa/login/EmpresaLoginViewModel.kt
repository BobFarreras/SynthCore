package com.deixebledenkaito.synthcore.ui.screen.empresa.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


@HiltViewModel
class EmpresaLoginViewModel @Inject constructor(
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _loginState = MutableStateFlow(EmpresaLoginState())
    val loginState: StateFlow<EmpresaLoginState> = _loginState.asStateFlow()

    fun onEmailChange(email: String) {
        _loginState.update { it.copy(email = email) }
    }

    fun onPasswordChange(password: String) {
        _loginState.update { it.copy(password = password) }
    }

    fun login() {
        _loginState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(
                    _loginState.value.email,
                    _loginState.value.password
                ).await()
                _loginState.update { it.copy(isLoginSuccess = true) }
            } catch (e: Exception) {
                _loginState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Error en l'inici de sessió"
                    )
                }
            }
        }
    }
}

data class EmpresaLoginState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isLoginSuccess: Boolean = false,
    val error: String? = null
)