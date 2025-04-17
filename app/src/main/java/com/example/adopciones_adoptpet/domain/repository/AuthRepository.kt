package com.example.adopciones_adoptpet.domain.repository

import com.example.adopciones_adoptpet.domain.model.User


interface AuthRepository {
    suspend fun logIn(email: String, password:String): Result<User>
    suspend fun logOut()
    fun getCurrentUser(): User?
}