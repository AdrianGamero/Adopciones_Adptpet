package com.example.adopciones_adoptpet.data.repository

import com.example.adopciones_adoptpet.domain.model.User
import com.example.adopciones_adoptpet.domain.repository.AuthRepository
import com.example.adopciones_adoptpet.firebase.FirebaseAuthService
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class AuthRepositoryImpl : AuthRepository {
    override suspend fun logIn(email: String, password: String): Result<User> {
        return suspendCancellableCoroutine { cont ->
            FirebaseAuthService.logIn(email, password) { result ->
                result.fold(
                    onSuccess = {
                        cont.resume(Result.success(User(it.uid)))
                    },
                    onFailure = {
                        cont.resume(Result.failure(it))
                    },


                    )
            }
        }
    }

    override suspend fun logOut() {
        FirebaseAuthService.logOut()
    }

    override fun getCurrentUser(): User? {
        val firebaseUser = FirebaseAuthService.currentUser()
        return firebaseUser?.let { User(it.uid) }
    }
}