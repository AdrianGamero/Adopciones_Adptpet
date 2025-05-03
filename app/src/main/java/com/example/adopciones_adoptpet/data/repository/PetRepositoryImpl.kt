package com.example.adopciones_adoptpet.data.repository

import android.graphics.Bitmap
import com.example.adopciones_adoptpet.data.dao.PetWithImagesDao
import com.example.adopciones_adoptpet.data.dataSource.FirebasePetDataSource
import com.example.adopciones_adoptpet.data.dataSource.RoomPetDataSource
import com.example.adopciones_adoptpet.domain.model.PetEntity
import com.example.adopciones_adoptpet.domain.model.PetImageEntity
import com.example.adopciones_adoptpet.domain.model.PetWithImages
import com.example.adopciones_adoptpet.domain.model.PetWithImagesAndBreeds
import com.example.adopciones_adoptpet.domain.repository.PetRepository
import com.example.adopciones_adoptpet.utils.Base64ToImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PetRepositoryImpl(
    private val dao: PetWithImagesDao,
    private val firebasePetDataSource: FirebasePetDataSource,
    private val roomPetDataSource: RoomPetDataSource
) : PetRepository{

    override suspend fun getAllPetsWithImages(): List<PetWithImages> {
        return dao.getAllPetsWithImages()
    }
    override suspend fun getPetsByBreed(breedId: String): List<PetEntity> {
        return dao.getPetsByBreed(breedId)
    }
    override suspend fun getPetsWithImagesAndBreeds():List<PetWithImagesAndBreeds>{
        val pets = roomPetDataSource.getAllPets()
        val breeds = roomPetDataSource.getAllBreeds()
        val images = roomPetDataSource.getAllImages()


        return pets.map{pet->
            val breedName = breeds.find { it.breedId == pet.breedId }?.name ?: ""
            val petImages = images.filter { it.petId == pet.petId }.map {it.url}
            val convertedImages = mutableListOf<Bitmap>()
                petImages.map { image ->
                    val convertedImage = Base64ToImage.decodeBase64(image)
                    convertedImages.add(convertedImage)
                }

            PetWithImagesAndBreeds(
                name = pet.name,
                images = convertedImages,
                age = pet.age,
                breedName = breedName,
                size = pet.size,
                gender = pet.gender
            )

        }
    }

    override suspend fun syncPetsWithRemote() = withContext(Dispatchers.IO) {
        val remotePets = firebasePetDataSource.getAllPets()
        val remoteBreeds= firebasePetDataSource.getAllBreeds()

        val remoteImages = mutableListOf<PetImageEntity>()

        val updatedPets = remotePets.map { pet->
            val images= firebasePetDataSource.getAllImages(pet.petId)
            remoteImages.addAll(images)
            pet.copy(lastUpdate = System.currentTimeMillis())
             }


        roomPetDataSource.insertAllBreeds(remoteBreeds)
        roomPetDataSource.insertAllPets(updatedPets)
        roomPetDataSource.insertAllImages(remoteImages)
    }
}