package com.example.adopciones_adoptpet.data.dataSource

import com.example.adopciones_adoptpet.domain.model.LoggedUserEntity
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class UserRemoteDataSource(    private val firestore: FirebaseFirestore = Firebase.firestore
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun getUser(uid: String): Result<LoggedUserEntity> = suspendCancellableCoroutine { cont ->
        firestore.collection("users").document(uid).get()
            .addOnSuccessListener { doc ->
                val user = LoggedUserEntity(
                    uid = uid,
                    name = doc.getString("name") ?: "",
                    email = doc.getString("email") ?: "",
                    role = doc.getString("role") ?: "",
                    phone = doc.getString("phone")
                )
                cont.resume(Result.success(user))
            }
            .addOnFailureListener {
                cont.resume(Result.failure(it))
            }
    }
}
