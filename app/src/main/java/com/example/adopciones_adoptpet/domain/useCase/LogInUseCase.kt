package com.example.adopciones_adoptpet.domain.useCase

import com.example.adopciones_adoptpet.domain.model.User
import com.example.adopciones_adoptpet.domain.repository.AuthRepository


class LogInUseCase(private val authRepository: AuthRepository) {
    suspend fun execute(email: String, password:String): Result<User>{
        return authRepository.logIn(email,password)
    }
}