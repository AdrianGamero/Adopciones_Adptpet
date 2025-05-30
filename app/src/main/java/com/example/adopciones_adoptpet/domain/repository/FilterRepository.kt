package com.example.adopciones_adoptpet.domain.repository

import com.example.adopciones_adoptpet.domain.model.Filter
import com.example.adopciones_adoptpet.domain.model.enums.PetType

interface FilterRepository {
    suspend fun getAnimalFilters(petType: PetType?): List<Filter>
    suspend fun getRequestFilters(): List<Filter>
}