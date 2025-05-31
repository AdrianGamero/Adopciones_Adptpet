package com.example.adopciones_adoptpet.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.adopciones_adoptpet.domain.model.enums.PetType

@Entity(tableName = "breeds")
data class BreedEntity(
    @PrimaryKey val breedId: String="",
    val name: String="",
    val type: PetType
)
