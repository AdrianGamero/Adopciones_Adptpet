package com.example.adopciones_adoptpet.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "pets")
data class PetEntity(
    @PrimaryKey val petId: String,
    val name:String,
    val age:Int,
    val gender: String,
    val size: String,
    val shelterId: String,
    val lastUpdate: Long,
    val synchronized: Boolean = false
)
