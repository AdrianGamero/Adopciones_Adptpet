package com.example.adopciones_adoptpet.data.dataSource

import com.example.adopciones_adoptpet.converters.Converters.toPetGender
import com.example.adopciones_adoptpet.converters.Converters.toPetSize
import com.example.adopciones_adoptpet.domain.model.BreedEntity
import com.example.adopciones_adoptpet.domain.model.PetEntity
import com.example.adopciones_adoptpet.domain.model.PetImageEntity

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.tasks.await
import com.example.adopciones_adoptpet.converters.Converters.toPetType
import com.google.firebase.firestore.SetOptions

class FirebasePetDataSource (
        private val db: FirebaseFirestore
    ) {
    val hasRemoteChanges = MutableStateFlow(false)


    init {
        observePets()
    }

    suspend fun getAllPets(): List<PetEntity> {
        val petList = mutableListOf<PetEntity>()
        val petDocs = db.collection(Constants.FirestoreConstants.PETS).get().await()

        for (doc in petDocs.documents) {
            val petId = doc.id
            val data = doc.data ?: continue

            val pet = PetEntity(
                petId = petId,
                name = data[Constants.FirestoreConstants.NAME] as? String ?: "",
                age = (data[Constants.FirestoreConstants.AGE] as? Long)?.toInt() ?: 0,
                gender = toPetGender(doc.getString(Constants.FirestoreConstants.GENDER)?: "Male"),
                size = toPetSize(doc.getString(Constants.FirestoreConstants.SIZE)?: "Medium"),
                breedId = data[Constants.FirestoreConstants.BREED_ID] as? String ?: "",
                shelterId = data[Constants.FirestoreConstants.SHELTER_ID] as? String ?: ""
            )

            petList.add(pet)
        }
        return petList
    }

    suspend fun getAllImages(): List<PetImageEntity>{
        val allImages = mutableListOf<PetImageEntity>()

        val pets = db.collection(Constants.FirestoreConstants.PETS).get().await()

        for (pet in pets.documents) {
            val petId = pet.id

            val images = db.collection(Constants.FirestoreConstants.PETS)
                .document(petId)
                .collection(Constants.FirestoreConstants.IMAGES)
                .get()
                .await()

            val petImages = images.documents.mapNotNull { doc ->
                doc.toObject(PetImageEntity::class.java)?.copy(petId = petId)
            }

            allImages.addAll(petImages)
        }

        return allImages
    }


    suspend fun getAllBreeds(): List<BreedEntity>{
        return db.collection(Constants.FirestoreConstants.BREEDS)
            .get()
            .await()
            .map { doc ->
                BreedEntity(
                    breedId = doc.id,
                    name = doc.getString(Constants.FirestoreConstants.NAME) ?: "",
                    type = toPetType(doc.getString(Constants.FirestoreConstants.TYPE) ?: "Perro")
                )
            }
    }

    fun observePets() {
        db.collection(Constants.FirestoreConstants.PETS).addSnapshotListener { snapshot, _ ->
            if (snapshot != null) hasRemoteChanges.value = true
        }

        db.collection(Constants.FirestoreConstants.BREEDS).addSnapshotListener { snapshot, _ ->
            if (snapshot != null) hasRemoteChanges.value = true
        }
    }

    fun resetRemoteChangeFlag() {
        hasRemoteChanges.value = false
    }

    suspend fun insertPetWithImages(
        pet: PetEntity,
        images: List<PetImageEntity>
    ) {

        val petDoc = db.collection(Constants.FirestoreConstants.PETS).document(pet.petId)
        petDoc.set(
            mapOf(
                Constants.FirestoreConstants.NAME to pet.name,
                Constants.FirestoreConstants.AGE to pet.age,
                Constants.FirestoreConstants.GENDER to pet.gender.displayName,
                Constants.FirestoreConstants.SIZE to pet.size.displayName,
                Constants.FirestoreConstants.BREED_ID to pet.breedId,
                Constants.FirestoreConstants.SHELTER_ID to pet.shelterId,
            )
        ).await()

        val imagesCollection = petDoc.collection(Constants.FirestoreConstants.IMAGES)
        images.forEach { image ->
            imagesCollection.document(image.imageId)
                .set(mapOf(
                    Constants.FirestoreConstants.IMAGE_ID to image.imageId,
                    Constants.FirestoreConstants.PET_ID to pet.petId,
                    Constants.FirestoreConstants.URL to image.url))
                .await()
        }
    }

     fun insertBreed(breed: BreedEntity) {
        db.collection(Constants.FirestoreConstants.BREEDS).document(breed.breedId).set(
            mapOf(
                Constants.FirestoreConstants.NAME to breed.name,
                Constants.FirestoreConstants.TYPE to breed.type.displayName
            ),
            SetOptions.merge()
        )
    }
}

