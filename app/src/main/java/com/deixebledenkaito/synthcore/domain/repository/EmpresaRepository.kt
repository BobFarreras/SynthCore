package com.deixebledenkaito.synthcore.domain.repository

import com.deixebledenkaito.synthcore.domain.model.Empresa

// domain/repository/EmpresaRepository.kt
interface EmpresaRepository {
    suspend fun registraEmpresa(empresa: Empresa): Result<String>
    suspend fun obtenirEmpresaPerCodi(codi: String): Result<Empresa>
}