package com.example.adopciones_adoptpet.converters

import androidx.room.TypeConverter
import com.example.adopciones_adoptpet.domain.model.enums.PetGender
import com.example.adopciones_adoptpet.domain.model.enums.PetSize
import com.example.adopciones_adoptpet.domain.model.enums.PetType
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

object Converters {
    @TypeConverter
    fun fromTypePet(value: PetType): String = value.displayName

    @TypeConverter
    fun toPetType(value: String): PetType = PetType.entries.firstOrNull { it.displayName.equals(value, ignoreCase = true)} ?: throw IllegalArgumentException("Unknown or null PetType value: $value")

    @TypeConverter
    fun fromGenderPet(value: PetGender): String = value.displayName

    @TypeConverter
    fun toPetGender(value: String): PetGender = PetGender.entries.firstOrNull { it.displayName.equals(value, ignoreCase = true)} ?: throw IllegalArgumentException("Unknown or null PetGender value: $value")

    @TypeConverter
    fun fromSizePet(value: PetSize): String = value.displayName

    @TypeConverter
    fun toPetSize(value: String): PetSize = PetSize.entries.firstOrNull { it.displayName.equals(value, ignoreCase = true)} ?: throw IllegalArgumentException("Unknown or null PetSize value: $value")


}