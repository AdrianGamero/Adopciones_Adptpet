package com.example.adopciones_adoptpet.data.dataSource

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.navigation.NavController
import com.example.adopciones_adoptpet.domain.model.LoggedUserEntity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
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

    suspend fun saveUser(user: LoggedUserEntity): Result<Unit> {
        return try {
            firestore.collection("user").document(user.uid).set(user).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
