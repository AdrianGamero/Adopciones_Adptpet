package com.example.adopciones_adoptpet.domain.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(tableName = "pets",
    foreignKeys = [ForeignKey(
        entity = BreedEntity::class,
        parentColumns = ["breedId"],
        childColumns = ["breedId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("breedId")]
    )
data class PetEntity(
    @PrimaryKey val petId: String,
    val name:String,
    val age:Int,
    val gender: String,
    val size: String,
    val breedId: String,
    val shelterId: String,
    val lastUpdate: Long,
    val synchronized: Boolean = false
)
