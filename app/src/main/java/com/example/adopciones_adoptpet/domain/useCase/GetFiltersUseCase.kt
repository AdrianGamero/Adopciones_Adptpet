package com.example.adopciones_adoptpet.domain.useCase

import com.example.adopciones_adoptpet.domain.model.Filter
import com.example.adopciones_adoptpet.domain.model.enums.PetType
import com.example.adopciones_adoptpet.domain.repository.FilterRepository

class GetFiltersUseCase(private val filterRepository: FilterRepository) {
    suspend operator fun invoke(petType: PetType?): List<Filter> {
        return filterRepository.getAnimalFilters(petType)
    }
}