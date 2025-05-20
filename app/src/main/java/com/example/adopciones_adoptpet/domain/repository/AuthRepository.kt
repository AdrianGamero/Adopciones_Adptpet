package com.example.adopciones_adoptpet.domain.repository

import com.example.adopciones_adoptpet.domain.model.LoggedUserEntity


interface AuthRepository {
    suspend fun logIn(email: String, password: String): Result<LoggedUserEntity>
    suspend fun logOut()
    suspend fun getCurrentUser(): LoggedUserEntity?
}