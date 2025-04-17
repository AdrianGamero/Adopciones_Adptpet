package com.example.adopciones_adoptpet.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "shelters")
data class ShelterEntity(
    @PrimaryKey val shelterId: String,
    val name: String,
    val location: String,
    val eMail: String,
    val phone: Int,
    val Synchronized: Boolean,
    val LastUpdate: Long
)
