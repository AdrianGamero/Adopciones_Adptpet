package com.example.adopciones_adoptpet.converters

import androidx.room.TypeConverter
import com.example.adopciones_adoptpet.domain.model.PetType

class Converters {
    @TypeConverter
    fun fromTypePet(value: PetType): String = value.name

    fun topetType(value: String): PetType = PetType.valueOf(value)
}