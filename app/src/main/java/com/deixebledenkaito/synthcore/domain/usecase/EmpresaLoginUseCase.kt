package com.deixebledenkaito.synthcore.domain.usecase

import com.deixebledenkaito.synthcore.domain.model.Empresa
import com.deixebledenkaito.synthcore.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

// domain/usecase/EmpresaLoginUseCase.kt
class EmpresaLoginUseCase @Inject constructor(
    private val authrepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<FirebaseUser> {
        return authrepository.loginEmpresa(email, password)
    }
}

// domain/usecase/EmpresaRegisterUseCase.kt
class EmpresaRegisterUseCase @Inject constructor(
    private val authrepository: AuthRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String,
        empresaData: Empresa
    ): Result<Pair<String, String>> {
        return authrepository.registerEmpresa(email, password, empresaData)
    }
}

// domain/usecase/LogoutUseCase.kt
class LogoutUseCase @Inject constructor(
    private val authrepository: AuthRepository
) {
    suspend operator fun invoke() {
        authrepository.logout()
    }
}