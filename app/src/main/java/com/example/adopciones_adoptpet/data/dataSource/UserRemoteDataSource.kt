package com.example.adopciones_adoptpet.data.dataSource

import com.example.adopciones_adoptpet.domain.model.LoggedUserEntity
import com.example.adopciones_adoptpet.domain.model.ShelterExtraData
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class UserRemoteDataSource(private val firestore: FirebaseFirestore = Firebase.firestore) {

    suspend fun getUser(uid: String): Result<LoggedUserEntity> {
        return try {
            val doc = firestore.collection(Constants.FirestoreConstants.USER_COLLECTION).document(uid).get().await()
            val user = LoggedUserEntity(
                uid = uid,
                name = doc.getString(Constants.FirestoreConstants.NAME) ?: "",
                email = doc.getString(Constants.FirestoreConstants.EMAIL) ?: "",
                role = doc.getString(Constants.FirestoreConstants.ROLE) ?: "",
                phone = doc.getLong(Constants.FirestoreConstants.PHONE)!!.toInt()
            )
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun getShelterExtra(uid:String): Result<ShelterExtraData>{
        return try {
            val doc = firestore.collection(Constants.FirestoreConstants.SHELTER_COLLECTION).document(uid).get().await()
            val shelterExtra= ShelterExtraData(
                uid= uid,
                address = doc.getString(Constants.FirestoreConstants.ADDRESS) ?: "",
                city = doc.getString(Constants.FirestoreConstants.CITY) ?: "",
                website = doc.getString(Constants.FirestoreConstants.WEBSITE) ?: "",

            )
            Result.success(shelterExtra)

        }catch (e: Exception){
            Result.failure(e)
        }
    }

    suspend fun saveUser(user: LoggedUserEntity): Result<Unit> {
        return try {
            val data = mapOf(
                Constants.FirestoreConstants.NAME to user.name,
                Constants.FirestoreConstants.EMAIL to user.email,
                Constants.FirestoreConstants.ROLE to user.role,
                Constants.FirestoreConstants.PHONE to user.phone
            )
            firestore.collection(Constants.FirestoreConstants.USER_COLLECTION).document(user.uid).set(data).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun saveShelterExtra(extraData: ShelterExtraData): Result<Unit> {
        return try {
            val data = mapOf(
                Constants.FirestoreConstants.ADDRESS to extraData.address,
                Constants.FirestoreConstants.CITY to extraData.city,
                Constants.FirestoreConstants.WEBSITE to extraData.website
            )
            firestore.collection(Constants.FirestoreConstants.SHELTER_COLLECTION).document(extraData.uid).set(data).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
