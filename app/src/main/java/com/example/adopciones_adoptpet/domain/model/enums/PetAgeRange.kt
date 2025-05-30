package com.example.adopciones_adoptpet.domain.model.enums

enum class PetAgeRange(val label: String, val min: Int?, val max: Int?) {
        UNDER_1("Menos de 1 año", null, 1),
        FROM_1_TO_2("1-2 años", 1, 2),
        FROM_2_TO_5("2-5 años", 2, 5),
        FROM_5_TO_10("5-10 años", 5, 10),
        OVER_10("Más de 10 años", 10, null),
        ALL("Todos", null, null);

        fun matches(age: Int): Boolean {
                return (min == null || age >= min) && (max == null || age < max)
        }
}