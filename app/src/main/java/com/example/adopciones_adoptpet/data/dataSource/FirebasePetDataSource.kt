package com.example.adopciones_adoptpet.data.dataSource

import com.example.adopciones_adoptpet.domain.model.BreedEntity
import com.example.adopciones_adoptpet.domain.model.PetEntity
import com.example.adopciones_adoptpet.domain.model.PetImageEntity
import com.example.adopciones_adoptpet.domain.model.PetType
import com.example.adopciones_adoptpet.domain.model.PetWithImages
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

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
            val images = db.collection("pets")
                .document(petId)
                .collection("images")
                .get()
                .await()


            val imageList = images.documents.mapNotNull { it.toObject(PetImageEntity::class.java)?.copy(petId=petId) }

            return imageList
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
