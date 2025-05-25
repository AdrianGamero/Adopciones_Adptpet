package com.example.adopciones_adoptpet.domain.model

data class UserWithExtraInfo(
    val user: LoggedUserEntity,
    val shelterExtraData: ShelterExtraData? = null
)
