package com.example.adopciones_adoptpet.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "requests")
data class RequestEntity(
    @PrimaryKey val requestId: String,
    val userId: String,
    val petId: String,
    val shelterId: String,
    val state: String,
    val date: Long,
    val synchronized: Boolean = false
)
