package com.example.adopciones_adoptpet.data.repository

import com.example.adopciones_adoptpet.data.dao.PetWithImagesDao
import com.example.adopciones_adoptpet.domain.model.Filter
import com.example.adopciones_adoptpet.domain.model.enums.PetAgeRange
import com.example.adopciones_adoptpet.domain.model.enums.PetDistanceRange
import com.example.adopciones_adoptpet.domain.model.enums.PetGender
import com.example.adopciones_adoptpet.domain.model.enums.PetSize
import com.example.adopciones_adoptpet.domain.model.enums.PetType
import com.example.adopciones_adoptpet.domain.repository.FilterRepository

class FilterRepositoryImpl(private val breedDao: PetWithImagesDao) : FilterRepository {
    override suspend fun getAnimalFilters(petType: PetType?): List<Filter> {
        val breedOptions = breedDao.getAllBreedsNames(petType)
        return listOf(
            Filter(
                Constants.FilterConstants.TYPE,
                PetType.entries.map { it.displayName }
            ),
            Filter(
                Constants.FilterConstants.SIZE,
                PetSize.entries.map { it.displayName }
            ),
            Filter(
                Constants.FilterConstants.AGE,
                PetAgeRange.entries.map { it.label }
            ),
            Filter(
                Constants.FilterConstants.GENDER,
                PetGender.entries.map { it.displayName }
            ),
            Filter(
                Constants.FilterConstants.DISTANCE,
                PetDistanceRange.entries.map { it.label }
            ),
            Filter(
                Constants.FilterConstants.BREED,
                listOf(Constants.FilterConstants.ALL) + breedOptions.ifEmpty { listOf(Constants.FilterConstants.NO_BREEDS_AVAILABLE) }
            )
        )
    }

    override suspend fun getRequestFilters(): List<Filter> {
        TODO("Not yet implemented")
    }
}