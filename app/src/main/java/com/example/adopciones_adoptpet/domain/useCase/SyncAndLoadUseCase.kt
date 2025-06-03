package com.example.adopciones_adoptpet.domain.useCase

import com.example.adopciones_adoptpet.domain.model.PetWithImagesAndBreeds
import com.example.adopciones_adoptpet.domain.repository.PetRepository
import kotlinx.coroutines.flow.Flow

class SyncAndLoadUseCase(private val petRepository: PetRepository) {
    suspend fun sync(shelterId: String?=null) {
        petRepository.syncPetsWithRemote(shelterId)
    }

    suspend fun invoke(): Flow<List<PetWithImagesAndBreeds>> {
        return petRepository.getPetsWithImagesAndBreeds()
    }

}