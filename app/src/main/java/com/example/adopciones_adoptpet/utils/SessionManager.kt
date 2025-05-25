package com.example.adopciones_adoptpet.utils

import com.example.adopciones_adoptpet.data.dao.UserDao
import com.example.adopciones_adoptpet.domain.model.LoggedUserEntity
import com.example.adopciones_adoptpet.domain.model.ShelterExtraData


class SessionManager(private val userDao: UserDao){
    suspend fun saveSession(user: LoggedUserEntity) {
        userDao.saveUser(user)
    }
    suspend fun saveExtraData(extraData: ShelterExtraData){
        userDao.saveExtraData(extraData)
    }

    suspend fun getSession(): LoggedUserEntity? {
        return userDao.getLoggedInUser()
    }

    suspend fun clearSession() {
        userDao.clearSession()
    }

}