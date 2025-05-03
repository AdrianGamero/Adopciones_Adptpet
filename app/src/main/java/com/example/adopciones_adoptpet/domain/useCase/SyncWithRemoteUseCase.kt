package com.example.adopciones_adoptpet.domain.useCase

import com.example.adopciones_adoptpet.domain.repository.PetRepository

class SyncWithRemoteUseCase(private val petRepository: PetRepository) {
    suspend fun invoke(){
        petRepository.syncPetsWithRemote()
    }
}