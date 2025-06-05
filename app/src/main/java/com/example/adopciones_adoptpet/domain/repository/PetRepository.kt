package com.example.adopciones_adoptpet.domain.repository

import com.example.adopciones_adoptpet.domain.model.BreedEntity
import com.example.adopciones_adoptpet.domain.model.PetWithImagesAndBreeds
import com.example.adopciones_adoptpet.domain.model.enums.PetType
import kotlinx.coroutines.flow.Flow

interface PetRepository {

    suspend fun syncPetsWithRemote(shelterId: String? = null)
    suspend fun getPetsWithImagesAndBreeds(): Flow<List<PetWithImagesAndBreeds>>
    suspend fun getBreedsByType(type: PetType): List<BreedEntity>
    suspend fun insertPet(pet: PetWithImagesAndBreeds, shelterId: String): Result<Unit>
}