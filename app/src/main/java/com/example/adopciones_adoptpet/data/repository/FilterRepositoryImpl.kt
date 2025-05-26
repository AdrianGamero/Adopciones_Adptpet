package com.example.adopciones_adoptpet.data.repository

import com.example.adopciones_adoptpet.data.dao.PetWithImagesDao
import com.example.adopciones_adoptpet.domain.model.Filter
import com.example.adopciones_adoptpet.domain.model.PetType
import com.example.adopciones_adoptpet.domain.repository.FilterRepository

class FilterRepositoryImpl(private val breedDao: PetWithImagesDao) : FilterRepository {
    override suspend fun getAnimalFilters(petType: PetType?): List<Filter> {
        val breedOptions = breedDao.getAllBreedsNames(petType)
        return listOf(
            Filter("Tipo", listOf("Perro", "Gato", "Todos")),
            Filter("Tamaño", listOf("Pequeño", "Mediano", "Grande", "Gigante", "Todos")),
            Filter(
                "Edad",
                listOf("<1 año", "1-2 años", "2-5 años", "5-10 años", ">10 años", "Todos")
            ),
            Filter("Genero", listOf("Macho", "Hembra", "Todos")),
            Filter("Distancia", listOf("<10 km", "<30 km", "<50 km", "<100km", ">100km")),
            Filter("Raza", listOf("Todos")+ breedOptions.ifEmpty { listOf("Sin razas disponibles") })
        )
    }

    override suspend fun getRequestFilters(): List<Filter> {
        TODO("Not yet implemented")
    }
}