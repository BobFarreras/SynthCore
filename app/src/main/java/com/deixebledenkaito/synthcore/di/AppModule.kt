// di/AppModule.kt
package com.deixebledenkaito.synthcore.di

import com.deixebledenkaito.synthcore.data.repository.AuthRepositoryImpl
import com.deixebledenkaito.synthcore.data.repository.EmpresaRepositoryImpl
import com.deixebledenkaito.synthcore.domain.repository.AuthRepository
import com.deixebledenkaito.synthcore.domain.repository.EmpresaRepository
import com.deixebledenkaito.synthcore.domain.usecase.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideEmpresaRepository(
        firestore: FirebaseFirestore
    ): EmpresaRepository = EmpresaRepositoryImpl(firestore)

    @Provides
    @Singleton
    fun provideAuthRepository(
        firebaseAuth: FirebaseAuth,
        empresaRepository: EmpresaRepository
    ): AuthRepository = AuthRepositoryImpl(firebaseAuth, empresaRepository)

    @Provides
    @Singleton
    fun provideRegistraEmpresaUseCase(
        repository: EmpresaRepository
    ): RegistraEmpresaUseCase = RegistraEmpresaUseCase(repository)

    @Provides
    @Singleton
    fun provideEmpresaLoginUseCase(
        authRepository: AuthRepository
    ): EmpresaLoginUseCase = EmpresaLoginUseCase(authRepository)

    @Provides
    @Singleton
    fun provideEmpresaRegisterUseCase(
        authRepository: AuthRepository
    ): EmpresaRegisterUseCase = EmpresaRegisterUseCase(authRepository)

    @Provides
    @Singleton
    fun provideLogoutUseCase(
        authRepository: AuthRepository
    ): LogoutUseCase = LogoutUseCase(authRepository)
}