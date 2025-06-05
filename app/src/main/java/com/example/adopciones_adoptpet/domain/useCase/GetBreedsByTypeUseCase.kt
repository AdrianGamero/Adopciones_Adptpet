package com.example.adopciones_adoptpet.domain.useCase

import com.example.adopciones_adoptpet.domain.model.BreedEntity
import com.example.adopciones_adoptpet.domain.model.enums.PetType
import com.example.adopciones_adoptpet.domain.repository.PetRepository

class GetBreedsByTypeUseCase (private val petRepository: PetRepository) {

    suspend fun invoke(petType: PetType): List<BreedEntity> {
        return petRepository.getBreedsByType(petType)
    }

}
