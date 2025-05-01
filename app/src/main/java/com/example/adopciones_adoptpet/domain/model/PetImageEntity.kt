package com.example.adopciones_adoptpet.domain.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    tableName = "pet_images",
    foreignKeys = [ForeignKey(
        entity = PetEntity::class,
        parentColumns = ["petId"],
        childColumns = ["petId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("petId")]
)
data class PetImageEntity(
    @PrimaryKey val imageId: Int,
    val petId: String,
    val url: String
)
