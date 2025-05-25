package com.example.adopciones_adoptpet.domain.useCase

import com.example.adopciones_adoptpet.domain.repository.AuthRepository

class SignUpUserUseCase(
    private val authRepository: AuthRepository
) {
    suspend fun signUpAdopter(
        name: String,
        email: String,
        password: String,
        phone: Int,
        role:String
    ): Result<Unit> {
        return authRepository.signUpUserAdopter(name, email, password, phone, role)
    }
    suspend fun signUpShelter(
        name: String,
        email: String,
        password: String,
        phone: Int,
        role:String,
        address: String,
        city:String,
        website: String
    ): Result<Unit>{
        return authRepository.signUpUserShelter(name, email, password, phone, role, address, city, website)

    }
}