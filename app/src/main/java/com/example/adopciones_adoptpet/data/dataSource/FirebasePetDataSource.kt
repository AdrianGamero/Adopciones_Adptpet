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

class FirebasePetDataSource (
        private val db: FirebaseFirestore
    ) {
    val hasRemoteChanges = MutableStateFlow(false)


    init {
        observePets()
    }

    suspend fun getAllPets(): List<PetEntity> {
        val petList = mutableListOf<PetEntity>()
        val petDocs = db.collection("pets").get().await()

        for (doc in petDocs.documents) {
            val petId = doc.id.toInt()
            val data = doc.data ?: continue

            val pet = PetEntity(
                petId = petId,
                name = data["name"] as? String ?: "",
                age = (data["age"] as? Long)?.toInt() ?: 0,
                gender = toPetGender(doc.getString("gender")?: "Male"),
                size = toPetSize(doc.getString("size")?: "Medium"),
                breedId = (data["breedId"] as? Long)?.toInt() ?: 0,
                shelterId = (data["shelterId"] as? Long)?.toInt() ?: 0,
            )

            petList.add(pet)
        }
        return petList
    }

    suspend fun getAllImages(): List<PetImageEntity>{
        val allImages = mutableListOf<PetImageEntity>()

        val pets = db.collection("pets").get().await()

        for (pet in pets.documents) {
            val petId = pet.id.toInt()

            val images = db.collection("pets")
                .document(petId.toString())
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
                    breedId = doc.id.toInt(),
                    name = doc.getString("name") ?: "",
                    type = toPetType(doc.getString("type") ?: "Perro")
                )
            }
    }

    fun observePets() {
        db.collection("pets").addSnapshotListener { snapshot, _ ->
            if (snapshot != null) hasRemoteChanges.value = true
        }

        db.collection("breeds").addSnapshotListener { snapshot, _ ->
            if (snapshot != null) hasRemoteChanges.value = true
        }
    }

    fun resetRemoteChangeFlag() {
        hasRemoteChanges.value = false
    }
}

