package com.example.adopciones_adoptpet.domain.model.enums

enum class PetAgeRange(val label: String, val minMonths: Int?, val maxMonths: Int?) {
        UNDER_1("Menos de 1 año", null, 12),
        FROM_1_TO_2("1-2 años", 12, 24),
        FROM_2_TO_5("2-5 años", 24, 60),
        FROM_5_TO_10("5-10 años", 60, 120),
        OVER_10("Más de 10 años", 120, null),
        ALL("Todos", null, null);

        fun matches(ageMonths: Int): Boolean {
                return (minMonths == null || ageMonths >= minMonths) && (maxMonths == null || ageMonths < maxMonths)
        }
}