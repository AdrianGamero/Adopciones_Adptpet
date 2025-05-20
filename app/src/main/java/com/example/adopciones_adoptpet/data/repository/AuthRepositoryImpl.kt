package com.example.adopciones_adoptpet.data.repository

import com.example.adopciones_adoptpet.data.dataSource.UserRemoteDataSource
import com.example.adopciones_adoptpet.domain.model.LoggedUserEntity
import com.example.adopciones_adoptpet.domain.repository.AuthRepository
import com.example.adopciones_adoptpet.firebase.FirebaseAuthService
import com.example.adopciones_adoptpet.utils.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class AuthRepositoryImpl(private val sessionManager: SessionManager,
                         private val remoteDataSource: UserRemoteDataSource
) : AuthRepository {
    override suspend fun logIn(email: String, password: String): Result<LoggedUserEntity> {
        return suspendCancellableCoroutine { cont ->
            FirebaseAuthService.logIn(email, password) { result ->
                result.fold(
                    onSuccess = { firebaseUser ->
                        val uid = firebaseUser.uid

                        CoroutineScope(Dispatchers.IO).launch {
                            val userResult = remoteDataSource.getUser(uid)
                            userResult.onSuccess { user ->
                                sessionManager.saveSession(user)
                                cont.resume(Result.success(user))
                            }.onFailure {
                                cont.resume(Result.failure(it))
                            }
                        }
                    },
                    onFailure = { cont.resume(Result.failure(it)) }
                )
            }
        }
    }

    override suspend fun logOut() {
        FirebaseAuthService.logOut()
        sessionManager.clearSession()
    }

    override suspend fun getCurrentUser(): LoggedUserEntity? {
        return sessionManager.getSession()
    }

}