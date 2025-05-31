package com.example.adopciones_adoptpet.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.adopciones_adoptpet.domain.model.LoggedUserEntity
import com.example.adopciones_adoptpet.domain.model.ShelterExtraData

@Dao
interface UserDao {

    @Query("SELECT * FROM logged_user LIMIT 1")
    suspend fun getLoggedInUser(): LoggedUserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUser(user: LoggedUserEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveExtraData(extraData: ShelterExtraData)

    @Query("DELETE FROM logged_user")
    suspend fun clearLoggedUser()

    @Query("DELETE FROM shelter_extra")
    suspend fun clearShelterExtraData()

    @Transaction
    suspend fun clearSession(){
        clearLoggedUser()
        clearShelterExtraData()
    }


}