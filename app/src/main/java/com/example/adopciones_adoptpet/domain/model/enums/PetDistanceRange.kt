package com.example.adopciones_adoptpet.domain.model.enums

enum class PetDistanceRange(val label: String, val min: Double?, val max: Double?) {
    UNDER_10("Menos de 10km", null, 10.0),
    FROM_10_TO_30("10-30km", 10.0, 30.0),
    FROM_30_TO_50("30-50km", 30.0, 50.0),
    FROM_50_TO_100("50-100km", 50.0, 100.0),
    OVER_10("Más de 100km", 100.0, null),
    ALL("Todos", null, null);
}