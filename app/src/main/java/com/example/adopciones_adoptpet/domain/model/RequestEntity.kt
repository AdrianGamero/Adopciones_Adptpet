package com.example.adopciones_adoptpet.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "requests")
data class RequestEntity(
    @PrimaryKey val requestId: String,
    val usuarioId: String,
    val animalId: String,
    val protectoraId: String,
    val estado: String,
    val mensaje: String?,
    val fecha: Long,
    val sincronizado: Boolean = false
)
