package com.deixebledenkaito.synthcore.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.deixebledenkaito.synthcore.domain.model.Empresa
import com.deixebledenkaito.synthcore.domain.repository.AuthRepository
import com.deixebledenkaito.synthcore.domain.usecase.EmpresaLoginUseCase
import com.deixebledenkaito.synthcore.domain.usecase.EmpresaRegisterUseCase
import com.deixebledenkaito.synthcore.domain.usecase.LogoutUseCase
import com.deixebledenkaito.synthcore.ui.screen.empresa.login.EmpresaLoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

// ui/viewmodel/EmpresaAuthViewModel.kt
@HiltViewModel
class EmpresaAuthViewModel @Inject constructor(
    private val loginUseCase: EmpresaLoginUseCase,
    private val registerUseCase: EmpresaRegisterUseCase,
    private val logoutUseCase: LogoutUseCase,
    val authRepository: AuthRepository
) : ViewModel() {
    private val TAG = "EmpresaAuthViewModel"

    // State per login
    private val _loginState = MutableStateFlow(EmpresaLoginState())
    val loginState: StateFlow<EmpresaLoginState> = _loginState.asStateFlow()

    // State per registre
    private val _registerState = MutableStateFlow(EmpresaRegisterState())
    val registerState: StateFlow<EmpresaRegisterState> = _registerState.asStateFlow()

    // Comprovar si l'usuari està loguejat
    init {
        if (authRepository.isUserLogged()) {
            _loginState.update { it.copy(isLoginSuccess = true) }
        }
    }

    // Funcions de canvi d'estat
    fun onEmailChange(email: String) {
        _loginState.update { it.copy(email = email) }
        _registerState.update { it.copy(email = email) }
    }

    fun onPasswordChange(password: String) {
        _loginState.update { it.copy(password = password) }
        _registerState.update { it.copy(password = password) }
    }

    fun onRegisterNomChange(nom: String) {
        _registerState.update { it.copy(nom = nom) }
    }

    fun onDireccioChange(direccio: String) {
        _registerState.update { it.copy(direccio = direccio) }
    }

    fun onCifChange(cif: String) {
        _registerState.update { it.copy(cif = cif) }
    }

    fun onLoginErrorConsumed() {
        _loginState.update { it.copy(error = null) }
    }

    fun onRegisterErrorConsumed() {
        _registerState.update { it.copy(error = null) }
    }

    suspend fun login() {
        Log.d(TAG, "Iniciant procés de login")
        _loginState.update { it.copy(isLoading = true, error = null) }

        loginUseCase(_loginState.value.email, _loginState.value.password).fold(
            onSuccess = { user ->
                Log.d(TAG, "Login exitós per l'usuari: ${user.uid}")
                _loginState.update {
                    it.copy(
                        isLoading = false,
                        isLoginSuccess = true,
                        error = null
                    )
                }
            },
            onFailure = { error ->
                Log.e(TAG, "Error en login: ${error.message}", error)
                _loginState.update {
                    it.copy(
                        isLoading = false,
                        error = error.message ?: "Error desconegut en login"
                    )
                }
            }
        )
    }

    suspend fun register() {
        Log.d(TAG, "Iniciant procés de registre")
        _registerState.update { it.copy(isLoading = true, error = null) }

        val empresa = Empresa(
            nom = _registerState.value.nom,
            email = _registerState.value.email,
            direccio = _registerState.value.direccio,
            cif = _registerState.value.cif
        )

        registerUseCase(_registerState.value.email, _registerState.value.password, empresa).fold(
            onSuccess = { (empresaId, codi) ->
                Log.d(TAG, "Registre exitós. ID: $empresaId, Codi: $codi")
                _registerState.update {
                    it.copy(
                        isLoading = false,
                        isRegisterSuccess = true,
                        empresaId = empresaId,
                        invitationCode = codi,
                        error = null
                    )
                }
            },
            onFailure = { error ->
                Log.e(TAG, "Error en registre: ${error.message}", error)
                _registerState.update {
                    it.copy(
                        isLoading = false,
                        error = error.message ?: "Error desconegut en registre"
                    )
                }
            }
        )
    }

    suspend fun logout() {
        try {
            Log.d(TAG, "Tancant sessió")
            logoutUseCase()
            // Reset states
            _loginState.update { EmpresaLoginState() }
            _registerState.update { EmpresaRegisterState() }
        } catch (e: Exception) {
            Log.e(TAG, "Error en logout: ${e.message}", e)
            throw e
        }
    }
}

data class EmpresaRegisterState(
    val nom: String = "",
    val email: String = "",
    val direccio: String = "",
    val cif: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isRegisterSuccess: Boolean = false,
    val empresaId: String? = null,
    val invitationCode: String? = null,
    val error: String? = null
)