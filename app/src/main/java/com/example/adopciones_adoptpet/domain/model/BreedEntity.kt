package com.example.adopciones_adoptpet.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "breeds")
data class BreedEntity(
    @PrimaryKey val breedId: String,
    val name: String,
    val type: PetType
)
