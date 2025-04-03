package com.deixebledenkaito.synthcore.domain.usecase

import com.deixebledenkaito.synthcore.domain.model.Empresa
import com.deixebledenkaito.synthcore.domain.repository.EmpresaRepository
import javax.inject.Inject

// domain/usecase/RegistraEmpresaUseCase.kt
class RegistraEmpresaUseCase @Inject constructor(
    private val repository: EmpresaRepository
) {
    suspend operator fun invoke(empresa: Empresa): Result<String> {
        return repository.registraEmpresa(empresa)
    }
}