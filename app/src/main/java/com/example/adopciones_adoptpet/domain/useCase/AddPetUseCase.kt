package com.example.adopciones_adoptpet.domain.useCase

import com.example.adopciones_adoptpet.domain.model.PetWithImagesAndBreeds
import com.example.adopciones_adoptpet.domain.repository.PetRepository

class AddPetUseCase(
    private val repository: PetRepository
) {
    suspend operator fun invoke(pet: PetWithImagesAndBreeds, shelterId: String): Result<Unit> {
        return repository.insertPet(pet, shelterId)
    }
}