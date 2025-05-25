package com.example.adopciones_adoptpet.domain.useCase

import com.example.adopciones_adoptpet.domain.model.LoggedUserEntity
import com.example.adopciones_adoptpet.domain.model.UserWithExtraInfo
import com.example.adopciones_adoptpet.domain.repository.AuthRepository


class LogInUseCase(private val authRepository: AuthRepository) {
    suspend fun logIn(email: String, password: String): Result<UserWithExtraInfo> {
        return authRepository.logIn(email, password)
    }
    suspend fun getSession(): LoggedUserEntity?{
        return authRepository.getCurrentUser()
    }
    suspend fun clearSession(){
        return authRepository.logOut()
    }
}