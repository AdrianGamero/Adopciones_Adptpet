package com.example.adopciones_adoptpet.converters

import androidx.room.TypeConverter
import com.example.adopciones_adoptpet.domain.model.PetType
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class Converters {
    @TypeConverter
    fun fromTypePet(value: PetType): String = value.name

    fun toPetType(value: String): PetType = PetType.valueOf(value)

    @TypeConverter
    fun fromDocumentReference(value: DocumentReference?): String? {
        return value?.path
    }

    @TypeConverter
    fun toDocumentReference(value: String?): DocumentReference? {
        return value?.let { FirebaseFirestore.getInstance().document(it) }
    }
}