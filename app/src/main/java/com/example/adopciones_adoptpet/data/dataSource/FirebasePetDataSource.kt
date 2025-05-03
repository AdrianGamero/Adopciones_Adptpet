package com.example.adopciones_adoptpet.data.dataSource

import com.example.adopciones_adoptpet.domain.model.BreedEntity
import com.example.adopciones_adoptpet.domain.model.PetEntity
import com.example.adopciones_adoptpet.domain.model.PetImageEntity
import com.example.adopciones_adoptpet.domain.model.PetType
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirebasePetDataSource (
        private val db: FirebaseFirestore
    ) {

        suspend fun getAllPets(): List<PetEntity>  {
            val petList = mutableListOf<PetEntity>()
            val pet = db.collection("pets").get().await()

            for (doc in pet.documents) {
                val petId = doc.id
                val petData = doc.toObject(PetEntity::class.java)?.copy(petId = petId)
                if (petData != null) {
                    petList.add(petData)
                }
            }


            return petList
        }

        suspend fun getAllImages(petId: String): List<PetImageEntity>{
            val allImages = mutableListOf<PetImageEntity>()

            val pets = db.collection("pets").get().await()

            for (pet in pets.documents) {
                val petId = pet.id

                val images = db.collection("pets")
                    .document(petId)
                    .collection("images")
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
        return db.collection("breeds")
            .get()
            .await()
            .map { doc ->
                BreedEntity(
                    breedId = doc.id,
                    name = doc.getString("name") ?: "",
                    type = PetType.valueOf(doc.getString("type")?.uppercase() ?: "DOG")
                )
            }
    }
    }
