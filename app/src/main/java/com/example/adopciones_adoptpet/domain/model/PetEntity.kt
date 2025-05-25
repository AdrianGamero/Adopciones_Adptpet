package com.example.adopciones_adoptpet.domain.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    tableName = "pets",
    foreignKeys = [ForeignKey(
        entity = BreedEntity::class,
        parentColumns = ["breedId"],
        childColumns = ["breedId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("breedId")]
)
data class PetEntity(
    @PrimaryKey val petId: Int =0,
    val name: String ="",
    val age: Int =0,
    val gender: String="",
    val size: String="",
    val breedId: Int=0,
    val shelterId: Int=0,
)
