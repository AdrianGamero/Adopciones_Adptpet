package com.example.adopciones_adoptpet.data.repository

import android.annotation.SuppressLint
import android.util.Log
import com.example.adopciones_adoptpet.data.dao.PetWithImagesDao
import com.example.adopciones_adoptpet.data.dataSource.FirebasePetDataSource
import com.example.adopciones_adoptpet.data.dataSource.RoomPetDataSource
import com.example.adopciones_adoptpet.domain.model.BreedEntity
import com.example.adopciones_adoptpet.domain.model.PetEntity
import com.example.adopciones_adoptpet.domain.model.PetImageEntity
import com.example.adopciones_adoptpet.domain.model.PetWithImagesAndBreeds
import com.example.adopciones_adoptpet.domain.model.enums.PetType
import com.example.adopciones_adoptpet.domain.repository.PetRepository
import com.example.adopciones_adoptpet.utils.Base64ToImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class PetRepositoryImpl(
    private val firebasePetDataSource: FirebasePetDataSource,
    private val roomPetDataSource: RoomPetDataSource
) : PetRepository {

    override suspend fun getBreedsByType(type: PetType): List<BreedEntity> {
        return roomPetDataSource.getAllBreeds().first().filter { it.type == type }
    }




    @SuppressLint("SuspiciousIndentation")
    override suspend fun getPetsWithImagesAndBreeds(): Flow<List<PetWithImagesAndBreeds>> {
        return combine(
            roomPetDataSource.getAllPets(),
            roomPetDataSource.getAllBreeds(),
            roomPetDataSource.getAllImages()
        ) { pets, breeds, images ->
            pets.map { pet ->
                val breedName = breeds.find { it.breedId == pet.breedId }?.name ?: ""
                val petType = breeds.find { it.breedId  ==pet.breedId}?.type!!
                val gender = pet.gender
                val size = pet.size
                val petImages = images.filter { it.petId == pet.petId }.map { it.url }
                val convertedImages = petImages.map { Base64ToImage.decodeBase64(it) }

                PetWithImagesAndBreeds(
                    name = pet.name,
                    images = convertedImages,
                    age = pet.age,
                    breedName = breedName,
                    size = size,
                    gender = gender,
                    petType = petType
                )
            }
        }
    }


    override suspend fun syncPetsWithRemote(): Unit = withContext(Dispatchers.IO) {
        val localPetsFlow = roomPetDataSource.getAllPets()
        val localBreedsFlow = roomPetDataSource.getAllBreeds()

        val localPets = localPetsFlow.first()
        val localBreeds = localBreedsFlow.first()

        val shouldForceSync = localPets.isEmpty() && localBreeds.isEmpty()

        if (!firebasePetDataSource.hasRemoteChanges.value && !shouldForceSync) {
            Log.d("sync", "No hay cambios remotos y la base de datos no está vacía. No se sincroniza.")
            return@withContext
        }

        val remotePets = firebasePetDataSource.getAllPets()
        val remoteBreeds = firebasePetDataSource.getAllBreeds()
        val remoteImages = mutableListOf<PetImageEntity>()

        val updatedPets = remotePets.map { pet ->
            val images = firebasePetDataSource.getAllImages()
            remoteImages.addAll(images)
            pet.copy()
        }

        val updatedBreeds = remoteBreeds.filter { remoteBreed ->
            val localBreed = localBreeds.find { it.breedId == remoteBreed.breedId }
            localBreed == null || localBreed.name != remoteBreed.name
        }

        if (updatedBreeds.isNotEmpty() || shouldForceSync) {
            Log.d("repo", "Razas introducidas")
            roomPetDataSource.insertAllBreeds(remoteBreeds)
        } else {
            Log.d("repo", "No hay cambios en las razas")
        }

        if (updatedPets.isNotEmpty() || shouldForceSync) {
            roomPetDataSource.insertAllPets(remotePets)
            Log.d("repo", "Animales introducidos")

            roomPetDataSource.insertAllImages(remoteImages)
            Log.d("repo", "Imagenes introducidas")
        } else {
            Log.d("repo", "No hay cambios en los animales")
        }

        firebasePetDataSource.resetRemoteChangeFlag()
    }

}