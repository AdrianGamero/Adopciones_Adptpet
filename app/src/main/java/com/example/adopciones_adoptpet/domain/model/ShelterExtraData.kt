package com.example.adopciones_adoptpet.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shelter_extra")
data class ShelterExtraData(
    @PrimaryKey val uid: String,
    val address: String,
    val city: String,
    val website:String
)
