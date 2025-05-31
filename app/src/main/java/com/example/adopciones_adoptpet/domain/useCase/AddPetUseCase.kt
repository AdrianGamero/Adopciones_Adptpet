package com.example.adopciones_adoptpet.domain.useCase

import android.graphics.Bitmap
import android.util.Log
import com.example.adopciones_adoptpet.domain.model.PetEntity
import com.example.adopciones_adoptpet.domain.model.PetWithImagesAndBreeds
import com.example.adopciones_adoptpet.domain.model.enums.PetGender
import com.example.adopciones_adoptpet.domain.model.enums.PetSize
import com.example.adopciones_adoptpet.domain.model.enums.PetType
import com.example.adopciones_adoptpet.domain.repository.PetRepository

class AddPetUseCase(
    private val repository: PetRepository
) {
    suspend operator fun invoke(pet: PetWithImagesAndBreeds, shelterId: String): Result<Unit> {
        Log.d("Insert pet", "useCase")
        return repository.insertPet(pet, shelterId)
    }
}