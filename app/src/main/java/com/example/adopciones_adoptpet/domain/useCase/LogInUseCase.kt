package com.example.adopciones_adoptpet.domain.useCase

import com.example.adopciones_adoptpet.domain.model.LoggedUserEntity
import com.example.adopciones_adoptpet.domain.repository.AuthRepository


class LogInUseCase(private val authRepository: AuthRepository) {
    suspend fun invoke(email: String, password: String): Result<LoggedUserEntity> {
        return authRepository.logIn(email, password)
    }
}