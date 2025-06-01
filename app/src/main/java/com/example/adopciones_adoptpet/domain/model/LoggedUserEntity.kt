package com.example.adopciones_adoptpet.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "logged_user")
data class LoggedUserEntity(
    @PrimaryKey val uid: String,
    val name: String,
    val email: String,
    val role: String,
    val phone: Int = 0
)
