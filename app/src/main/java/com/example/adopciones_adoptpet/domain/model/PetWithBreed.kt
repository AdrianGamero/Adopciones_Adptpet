package com.example.adopciones_adoptpet.domain.model

import androidx.room.Embedded
import androidx.room.Relation

data class PetWithBreed(
    @Embedded val pet: PetEntity,
    @Relation(
        parentColumn = "breedId",
        entityColumn = "breedId"
    )
    val breed: BreedEntity
    )
