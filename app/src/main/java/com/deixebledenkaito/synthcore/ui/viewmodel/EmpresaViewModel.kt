package com.deixebledenkaito.synthcore.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

import com.deixebledenkaito.synthcore.data.model.Empresa
import com.deixebledenkaito.synthcore.domain.usecase.RegistraEmpresaUseCase
import com.deixebledenkaito.synthcore.utils.CodiInvitacioUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

// ui/viewmodel/EmpresaViewModel.kt
@HiltViewModel
class EmpresaViewModel @Inject constructor(
    private val registraEmpresaUseCase: RegistraEmpresaUseCase
) : ViewModel() {

    var state by mutableStateOf(EmpresaState())
        private set

    fun onNomChange(nom: String) {
        state = state.copy(nom = nom)
    }

    suspend fun registraEmpresa() {
        val empresa = Empresa(
            nom = state.nom,
            cif = state.cif,
            codiInvitacio = CodiInvitacioUtil.generarCodi()
        )

        registraEmpresaUseCase(empresa).fold(
            onSuccess = { empresaId ->
                state = state.copy(
                    codiInvitacio = empresa.codiInvitacio,
                    isSuccess = true
                )
            },
            onFailure = { error ->
                state = state.copy(error = error.message)
            }
        )
    }
}

data class EmpresaState(
    val nom: String = "",
    val cif: String = "",
    val codiInvitacio: String = "",
    val isSuccess: Boolean = false,
    val error: String? = null
)