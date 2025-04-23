package com.example.adopciones_adoptpet.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.adopciones_adoptpet.domain.model.PetType
import com.example.adopciones_adoptpet.domain.model.PetWithImagesAndBreed

@Dao
interface PetWithImagesDao {
    @Transaction

    @Query("SELECT * FROM pets WHERE petId = :petId")
    suspend fun getPetWithImages(petId: String): PetWithImagesAndBreed

    @Query("SELECT * FROM breeds WHERE :petType IS NULL OR type= :petType")
    suspend fun getAllBreedsNames(petType: PetType?): List<String>
}