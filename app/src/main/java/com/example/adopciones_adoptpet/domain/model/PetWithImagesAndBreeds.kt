package com.example.adopciones_adoptpet.domain.model

import android.graphics.Bitmap

data class PetWithImagesAndBreeds(
    val name: String,
    val images:List<Bitmap>,
    val age: Int,
    val breedName: String,
    val size: String,
    val gender: String,
    val petType: PetType
)
