package com.deixebledenkaito.synthcore.ui.screen.empresa

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import com.deixebledenkaito.synthcore.domain.repository.AuthRepository
import com.deixebledenkaito.synthcore.domain.usecase.EmpresaLoginUseCase
import com.deixebledenkaito.synthcore.domain.usecase.EmpresaRegisterUseCase
import com.deixebledenkaito.synthcore.domain.usecase.LogoutUseCase
import com.deixebledenkaito.synthcore.ui.screen.empresa.login.EmpresaLoginState
import com.deixebledenkaito.synthcore.ui.screen.empresa.registre.EmpresaRegisterState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class IniciEmpresaViewModel @Inject constructor(

    private val logoutUseCase: LogoutUseCase,

) : ViewModel() {
    // State per login
    val _loginState = MutableStateFlow(EmpresaLoginState())
    val loginState: StateFlow<EmpresaLoginState> = _loginState.asStateFlow()

    // State per registre
    private val _registerState = MutableStateFlow(EmpresaRegisterState())
    val registerState: StateFlow<EmpresaRegisterState> = _registerState.asStateFlow()

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