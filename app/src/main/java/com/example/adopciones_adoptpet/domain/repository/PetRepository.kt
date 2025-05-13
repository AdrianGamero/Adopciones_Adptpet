package com.example.adopciones_adoptpet.domain.repository

import com.example.adopciones_adoptpet.domain.model.PetEntity
import com.example.adopciones_adoptpet.domain.model.PetWithImagesAndBreeds
import kotlinx.coroutines.flow.Flow

interface PetRepository {
    suspend fun getPetsByBreed(breedId: String): List<PetEntity>
    suspend fun syncPetsWithRemote()
    suspend fun getPetsWithImagesAndBreeds(): Flow<List<PetWithImagesAndBreeds>>
}