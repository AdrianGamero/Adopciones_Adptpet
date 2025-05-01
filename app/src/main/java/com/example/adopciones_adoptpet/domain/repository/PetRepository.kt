package com.example.adopciones_adoptpet.domain.repository

import com.example.adopciones_adoptpet.domain.model.PetEntity
import com.example.adopciones_adoptpet.domain.model.PetType
import com.example.adopciones_adoptpet.domain.model.PetWithImages
import com.example.adopciones_adoptpet.domain.model.PetWithImagesAndBreeds

interface PetRepository {
    suspend fun getAllPetsWithImages(): List<PetWithImages>
    suspend fun getPetsByBreed(breedId: String): List<PetEntity>
    suspend fun syncPetsWithRemote()
    suspend fun getPetsWithImagesAndBreeds():List<PetWithImagesAndBreeds>
}