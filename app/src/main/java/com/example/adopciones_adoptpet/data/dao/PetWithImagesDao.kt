package com.example.adopciones_adoptpet.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.adopciones_adoptpet.domain.model.BreedEntity
import com.example.adopciones_adoptpet.domain.model.PetEntity
import com.example.adopciones_adoptpet.domain.model.PetImageEntity
import com.example.adopciones_adoptpet.domain.model.PetType
import com.example.adopciones_adoptpet.domain.model.PetWithImages

@Dao
interface PetWithImagesDao {

    @Transaction
    @Query("SELECT * FROM pets WHERE petId = :petId")
    suspend fun getPetWithImages(petId: String): PetWithImages

    @Query("SELECT * FROM pets")
    suspend fun getAllPetsWithImages(): List<PetWithImages>

    @Transaction
    @Query("SELECT * FROM pets")
    suspend fun getAllPets(): List<PetEntity>

    @Query("SELECT name FROM breeds WHERE :petType IS NULL OR type= :petType")
    suspend fun getAllBreedsNames(petType: PetType?): List<String>

    @Query("SELECT * FROM pets WHERE breedId = :breedId ")
    suspend fun getPetsByBreed(breedId: String): List<PetEntity>

    @Query("SELECT * FROM pet_images WHERE petId = :petId")
    suspend fun getAllImagesByPetId(petId: String): List<PetImageEntity>

    @Query("SELECT * FROM pet_images")
    suspend fun getAllImages(): List<PetImageEntity>

    @Query("SELECT * FROM breeds")
    suspend fun getAllbreeds(): List<BreedEntity>

    @Insert
    fun insertAllPets(pets: List<PetEntity>)

    @Insert
    fun insertAllImages(images: List<PetImageEntity>)

    @Insert
    fun insertAllBreeds(breeds: List<BreedEntity>)



}