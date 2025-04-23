package com.example.adopciones_adoptpet.data.repository

import com.example.adopciones_adoptpet.data.dao.PetWithImagesDao
import com.example.adopciones_adoptpet.domain.model.PetType
import com.example.adopciones_adoptpet.domain.repository.PetRepository

class PetRepositoryImpl(private val dao:PetWithImagesDao) : PetRepository {

    override suspend fun getBreeds(petType: PetType?): List<String> {
        return dao.getAllBreedsNames(petType)
    }
}