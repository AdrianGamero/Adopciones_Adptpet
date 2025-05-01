package com.example.adopciones_adoptpet.domain.model

data class PetWithImagesAndBreeds(
    val name: String,
    val images: List<String>,
    val age: Int,
    val breedName: String,
    val size: String,
    val gender: String
)
