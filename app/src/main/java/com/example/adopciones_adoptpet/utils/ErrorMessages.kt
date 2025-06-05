package com.example.adopciones_adoptpet.utils

object ErrorMessages {
    const val UNKNOWN_PET_TYPE = "Unknown or null PetType value: %s"
    const val UNKNOWN_PET_GENDER = "Unknown or null PetGender value: %s"
    const val UNKNOWN_PET_SIZE = "Unknown or null PetSize value: %s"

    const val USER_ALREADY_EXISTS = "El usuario ya está registrado."
    const val WEAK_PASSWORD = "La contraseña es demasiado débil."
    const val INVALID_EMAIL = "La dirección de correo es inválida."
    fun authError(detail: String?) = "Error de autenticación: ${detail ?: ""}"
    fun unknownError(detail: String?) = "Error desconocido: ${detail ?: ""}"

    const val INVALID_PHONE_NUMBER= "Número de teléfono inválido"

    const val INVALID_CREDENTIALS = "Correo o contraseña incorrectos"
    const val NETWORK_ERROR = "Error de red. Verifica tu conexión"
    const val UNKNOWN_ERROR = "Error al iniciar sesión. Inténtalo más tarde"
}