package com.example.adopciones_adoptpet.domain.repository

import com.example.adopciones_adoptpet.domain.model.PetType

interface PetRepository {
    suspend fun getBreeds (petType:PetType?): List<String>
}