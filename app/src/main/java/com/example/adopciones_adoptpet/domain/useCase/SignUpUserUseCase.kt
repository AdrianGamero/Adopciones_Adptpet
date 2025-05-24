package com.example.adopciones_adoptpet.domain.useCase

import com.example.adopciones_adoptpet.domain.repository.AuthRepository

class SignUpUserUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        name: String,
        email: String,
        password: String,
        phone: Int
    ): Result<Unit> {
        return authRepository.signUpUser(name, email, password, phone)
    }
}