package com.example.adopciones_adoptpet.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RequestEntity(
    @PrimaryKey val id: String, // Igual que el ID en Firestore
    val usuarioId: String,
    val animalId: String,
    val protectoraId: String,
    val estado: String, // "pendiente", "aceptada", "rechazada"
    val mensaje: String?,
    val fecha: Long, // timestamp en milisegundos
    val sincronizado: Boolean = false // true si ya fue subida a Firestore
)
