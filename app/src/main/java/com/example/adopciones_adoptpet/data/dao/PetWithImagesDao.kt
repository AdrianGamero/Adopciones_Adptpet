package com.example.adopciones_adoptpet.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.adopciones_adoptpet.domain.model.PetWithImages

@Dao
interface PetWithImagesDao {
    @Transaction

    @Query("SELECT * FROM pets WHERE petId = :petId")
    suspend fun getPetWithImages(petId: String): PetWithImages

}