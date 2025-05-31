package com.example.adopciones_adoptpet.data.dataSource

import android.graphics.Bitmap
import com.example.adopciones_adoptpet.data.dao.PetWithImagesDao
import com.example.adopciones_adoptpet.domain.model.BreedEntity
import com.example.adopciones_adoptpet.domain.model.PetEntity
import com.example.adopciones_adoptpet.domain.model.PetImageEntity
import com.example.adopciones_adoptpet.domain.model.PetWithImagesAndBreeds
import com.example.adopciones_adoptpet.utils.ImageToBase64.encodeBitmapToBase64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.util.UUID


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

        suspend fun getAllPets():Flow<List<PetEntity>>{
            return dao.getAllPets()
        }
        suspend fun getAllBreeds(): Flow<List<BreedEntity>>{
            return dao.getAllBreeds()
        }
        suspend fun getAllImages(): Flow<List<PetImageEntity>>{
            return dao.getAllImages()
        }



    suspend fun getBreeds(): List<BreedEntity> {
        return dao.getAllBreedsNow()
    }

    suspend fun insertPetWithImages(pet: PetEntity, images: List<PetImageEntity>) = withContext(Dispatchers.IO) {
        dao.insertPet(pet)
        dao.insertImages(images)
    }

    suspend fun insertBreed(breed: BreedEntity) = withContext(Dispatchers.IO) {
        dao.insertBreed(breed)
    }



}
