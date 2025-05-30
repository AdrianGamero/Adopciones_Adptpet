package com.example.adopciones_adoptpet.domain.model

import android.graphics.Bitmap
import com.example.adopciones_adoptpet.domain.model.enums.PetGender
import com.example.adopciones_adoptpet.domain.model.enums.PetSize
import com.example.adopciones_adoptpet.domain.model.enums.PetType

data class PetWithImagesAndBreeds(
    val name: String,
    val images:List<Bitmap>,
    val age: Int,
    val breedName: String,
    val size: PetSize,
    val gender: PetGender,
    val petType: PetType
)
