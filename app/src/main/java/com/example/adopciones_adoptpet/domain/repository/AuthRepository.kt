package com.example.adopciones_adoptpet.domain.repository

import android.provider.ContactsContract.CommonDataKinds.Website
import com.example.adopciones_adoptpet.domain.model.LoggedUserEntity


interface AuthRepository {
    suspend fun logIn(email: String, password: String): Result<LoggedUserEntity>
    suspend fun logOut()
    suspend fun getCurrentUser(): LoggedUserEntity?
    suspend fun signUpUserAdopter(
        name: String,
        email: String,
        password: String,
        phone: Int,
        role:String
    ): Result<Unit>

    suspend fun signUpUserShelter(
        name: String,
        email: String,
        password: String,
        phone: Int,
        role:String,
        address: String,
        city: String,
        website: String
    ): Result<Unit>

}