package com.example.adopciones_adoptpet.utils

object FormatBreedName {
    fun formatBreedName(input: String): String {
        return input.trim()
            .lowercase()
            .split(Regex("\\s+"))
            .joinToString(" ") { it.replaceFirstChar(Char::uppercaseChar) }
    }
}