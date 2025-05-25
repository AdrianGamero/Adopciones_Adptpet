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
            val doc = firestore.collection("user").document(uid).get().await()
            val user = LoggedUserEntity(
                uid = uid,
                name = doc.getString("name") ?: "",
                email = doc.getString("email") ?: "",
                role = doc.getString("role") ?: "",
                phone = doc.getLong("phone")!!.toInt()
            )
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun getShelterExtra(uid:String): Result<ShelterExtraData>{
        return try {
            val doc = firestore.collection("shelter").document(uid).get().await()
            val shelterExtra= ShelterExtraData(
                uid= uid,
                address = doc.getString("address") ?: "",
                city = doc.getString("city") ?: "",
                website = doc.getString("website") ?: "",
            )
            Result.success(shelterExtra)

        }catch (e: Exception){
            Result.failure(e)
        }
    }

    suspend fun saveUser(user: LoggedUserEntity): Result<Unit> {
        return try {
            val data = mapOf(
                "name" to user.name,
                "email" to user.email,
                "role" to user.role,
                "phone" to user.phone
            )
            firestore.collection("user").document(user.uid).set(data).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun saveShelterExtra(extraData: ShelterExtraData): Result<Unit> {
        return try {
            val data = mapOf(
                "address" to extraData.address,
                "city" to extraData.city,
                "website" to extraData.website
            )
            firestore.collection("shelter").document(extraData.uid).set(data).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
