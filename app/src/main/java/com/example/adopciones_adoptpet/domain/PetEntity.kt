package com.example.adopciones_adoptpet.domain

import androidx.room.Entity


@Entity
data class PetEntity(
    val name:String,
    val age:Int,
    val gender: String,
    val size: String,
    val images: List<String>
)
