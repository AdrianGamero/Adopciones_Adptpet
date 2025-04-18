package com.example.adopciones_adoptpet.domain.model

import androidx.room.Embedded
import androidx.room.Relation

data class PetWithImagesAndBreed(
    @Embedded val pet: PetEntity,
    @Relation(
        parentColumn = "petId",
        entityColumn = "petId"
    )
    val images: List<PetImageEntity>,

    @Relation(
        parentColumn = "breedId",
        entityColumn = "breedId"
    )
    val breed: BreedEntity
)
