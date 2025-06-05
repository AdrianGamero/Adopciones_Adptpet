package com.example.adopciones_adoptpet.data.repository

import android.annotation.SuppressLint
import android.util.Log
import com.example.adopciones_adoptpet.data.dataSource.FirebasePetDataSource
import com.example.adopciones_adoptpet.data.dataSource.RoomPetDataSource
import com.example.adopciones_adoptpet.domain.model.BreedEntity
import com.example.adopciones_adoptpet.domain.model.PetEntity
import com.example.adopciones_adoptpet.domain.model.PetImageEntity
import com.example.adopciones_adoptpet.domain.model.PetWithImagesAndBreeds
import com.example.adopciones_adoptpet.domain.model.enums.PetType
import com.example.adopciones_adoptpet.domain.repository.PetRepository
import com.example.adopciones_adoptpet.utils.Base64ToImage
import com.example.adopciones_adoptpet.utils.FormatPetData.formatBreedName
import com.example.adopciones_adoptpet.utils.ImageToBase64.encodeBitmapToBase64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.util.UUID

class PetRepositoryImpl(
    private val firebasePetDataSource: FirebasePetDataSource,
    private val roomPetDataSource: RoomPetDataSource
) : PetRepository {
    private var lastSyncedUserId: String? = null

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
                val petImages = images.filter { it.petId.equals(pet.petId) }.map { it.url }
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


    override suspend fun syncPetsWithRemote(shelterId: String?): Unit = withContext(Dispatchers.IO) {
        val currentUserId = shelterId ?: "PUBLIC"
        val isFirstSyncForUser = currentUserId != lastSyncedUserId
        lastSyncedUserId = currentUserId

        val localPets = roomPetDataSource.getAllPets().first()
        val localBreeds = roomPetDataSource.getAllBreeds().first()

        val shouldForceSync = (localPets.isEmpty() && localBreeds.isEmpty())  ||  isFirstSyncForUser

        if (!firebasePetDataSource.hasRemoteChanges.value && !shouldForceSync) {
            Log.d("sync", "No hay cambios remotos y no se requiere sincronización forzada.")
            return@withContext
        }

        val remotePets = firebasePetDataSource.getAllPets()
        val remoteBreeds = firebasePetDataSource.getAllBreeds()
        val remoteImages = firebasePetDataSource.getAllImages()

        val filteredPets = if (shelterId != null) {
            remotePets.filter { it.shelterId == shelterId }
        } else {
            remotePets
        }

        val filteredImages = remoteImages.filter { img ->
            filteredPets.any { it.petId == img.petId }
        }

        val petsToDelete = if (shelterId != null) {
            localPets.filter { it.shelterId != shelterId || filteredPets.none { r -> r.petId == it.petId } }
        } else {
            localPets.filter { localPet -> filteredPets.none { r -> r.petId == localPet.petId } }
        }

        if (petsToDelete.isNotEmpty()) {
            val idsToDelete = petsToDelete.map { it.petId }
            roomPetDataSource.deleteImagesByPetIds(idsToDelete)
            idsToDelete.forEach { roomPetDataSource.deletePetById(it) }
            Log.d("sync", "Eliminados ${idsToDelete.size} animales e imágenes que no aplican.")
        }

        val updatedBreeds = remoteBreeds.filter { remoteBreed ->
            val localBreed = localBreeds.find { it.breedId == remoteBreed.breedId }
            localBreed == null || localBreed.name != remoteBreed.name
        }

        if (updatedBreeds.isNotEmpty() || shouldForceSync) {
            roomPetDataSource.insertAllBreeds(remoteBreeds)
            Log.d("repo", "Razas insertadas o actualizadas.")
        } else {
            Log.d("repo", "No hay cambios en las razas.")
        }

        if (filteredPets.isNotEmpty() || shouldForceSync) {
            roomPetDataSource.insertAllPets(filteredPets)
            roomPetDataSource.insertAllImages(filteredImages)
            Log.d("repo", "Animales e imágenes sincronizados.")
        } else {
            Log.d("repo", "No hay cambios en los animales.")
        }

        firebasePetDataSource.resetRemoteChangeFlag()
    }



    override suspend fun insertPet(pet: PetWithImagesAndBreeds, shelterId: String): Result<Unit> {
        Log.d("insert pet", "repositorio llamado")
        return try {
            val existingBreeds = roomPetDataSource.getBreeds()

            val formattedBreedName = formatBreedName(pet.breedName)
            val existingBreed = existingBreeds.find { it.name == formattedBreedName }

            val breed = existingBreed ?: BreedEntity(
                breedId = UUID.randomUUID().toString(),
                name = formattedBreedName,
                type = pet.petType
            ).also {
                roomPetDataSource.insertBreed(it)
                firebasePetDataSource.insertBreed(it)
            }

            val petId = UUID.randomUUID().toString()

            val petEntity = PetEntity(
                petId = petId,
                name = pet.name,
                age = pet.age,
                gender = pet.gender,
                size = pet.size,
                breedId = breed.breedId,
                shelterId = shelterId
            )

            val imageEntities = pet.images.map { bitmap ->
                PetImageEntity(
                    imageId = UUID.randomUUID().toString(),
                    petId = petId,
                    url = encodeBitmapToBase64(bitmap)
                )
            }
            Log.d("insert pet", "insertando en room")

            roomPetDataSource.insertPetWithImages(petEntity, imageEntities)
            Log.d("insert pet", "insertando en firebase")

            firebasePetDataSource.insertPetWithImages(petEntity, imageEntities)

            Result.success(Unit)

        } catch (e: Exception) {
            Log.d("insert pet", e.toString())

            Result.failure(e)

        }
    }
}