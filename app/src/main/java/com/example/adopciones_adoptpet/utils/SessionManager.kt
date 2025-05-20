package com.example.adopciones_adoptpet.utils

import com.example.adopciones_adoptpet.data.dao.UserDao
import com.example.adopciones_adoptpet.domain.model.LoggedUserEntity


class SessionManager(private val userDao: UserDao){
    suspend fun saveSession(user: LoggedUserEntity) {
        userDao.saveUser(user)
    }

    suspend fun getSession(): LoggedUserEntity? {
        return userDao.getLoggedInUser()
    }

    suspend fun clearSession() {
        userDao.clearSession()
    }
}