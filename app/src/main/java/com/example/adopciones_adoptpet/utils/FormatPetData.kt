package com.example.adopciones_adoptpet.utils

import android.content.Context
import com.example.adopciones_adoptpet.R

object FormatPetData {
    fun formatBreedName(input: String): String {
        return input.trim()
            .lowercase()
            .split(Regex("\\s+"))
            .joinToString(" ") { it.replaceFirstChar(Char::uppercaseChar) }
    }
    fun formatAge(months: Int, context: Context): String {
        return when {
            months < 12 -> context.getString(R.string.month_label, months)
            months % 12 == 0 -> context.getString(R.string.year_label, months/12)
            else -> context.getString(R.string.years_and_months_label, months / 12, months % 12)
        }
    }
}