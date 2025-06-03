package com.example.adopciones_adoptpet.data.dataSource

import com.example.adopciones_adoptpet.data.dao.PetWithImagesDao
import com.example.adopciones_adoptpet.domain.model.BreedEntity
import com.example.adopciones_adoptpet.domain.model.PetEntity
import com.example.adopciones_adoptpet.domain.model.PetImageEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext


class RoomPetDataSource(private val dao: PetWithImagesDao) {

        suspend fun insertAllPets(pets: List<PetEntity>)  = withContext(Dispatchers.IO){
            dao.insertAllPets(pets)
        }

        suspend fun insertAllImages(images: List<PetImageEntity>) = withContext(Dispatchers.IO) {
            dao.insertAllImages(images)
        }

        suspend fun insertAllBreeds(breeds: List<BreedEntity>) = withContext(Dispatchers.IO) {
            dao.insertAllBreeds(breeds)
        }

         fun getAllPets():Flow<List<PetEntity>>{
            return dao.getAllPets()
        }
         fun getAllBreeds(): Flow<List<BreedEntity>>{
            return dao.getAllBreeds()
        }
         fun getAllImages(): Flow<List<PetImageEntity>>{
            return dao.getAllImages()
        }

    suspend fun deletePetById(petId: String) = withContext(Dispatchers.IO) {
        dao.deleteImagesByPetId(petId)
        dao.deletePetById(petId)
    }


     fun getBreeds(): List<BreedEntity> {
        return dao.getAllBreedsNow()
    }

    suspend fun deleteImagesByPetIds(petIds: List<String>) {
        dao.deleteImagesByPetIds(petIds)
    }

    suspend fun insertPetWithImages(pet: PetEntity, images: List<PetImageEntity>) = withContext(Dispatchers.IO) {
        dao.insertPet(pet)
        dao.insertImages(images)
    }

    suspend fun insertBreed(breed: BreedEntity) = withContext(Dispatchers.IO) {
        dao.insertBreed(breed)
    }
}
