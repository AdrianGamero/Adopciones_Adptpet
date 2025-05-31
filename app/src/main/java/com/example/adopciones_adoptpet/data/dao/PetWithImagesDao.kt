package com.example.adopciones_adoptpet.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.adopciones_adoptpet.domain.model.BreedEntity
import com.example.adopciones_adoptpet.domain.model.PetEntity
import com.example.adopciones_adoptpet.domain.model.PetImageEntity
import com.example.adopciones_adoptpet.domain.model.enums.PetType
import kotlinx.coroutines.flow.Flow

@Dao
interface PetWithImagesDao {

    @Query("SELECT name FROM breeds WHERE :petType IS NULL OR type= :petType")
    suspend fun getAllBreedsNames(petType: PetType?): List<String>

    @Query("SELECT * FROM breeds")
    fun getAllBreedsNow(): List<BreedEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPets(pets: List<PetEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllImages(images: List<PetImageEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllBreeds(breeds: List<BreedEntity>)

    @Query("SELECT * FROM pets")
    fun getAllPets(): Flow<List<PetEntity>>

    @Query("SELECT * FROM breeds")
    fun getAllBreeds(): Flow<List<BreedEntity>>

    @Query("SELECT * FROM pet_images")
    fun getAllImages(): Flow<List<PetImageEntity>>

    @Insert
    suspend fun insertPet(pet: PetEntity): Long
    @Insert
    suspend fun insertBreed(breed: BreedEntity): Long

    @Insert
    suspend fun insertImages(images: List<PetImageEntity>)

    @Query("SELECT * FROM breeds WHERE name = :breedName LIMIT 1")
    suspend fun getBreedByName(breedName: String): BreedEntity?

}