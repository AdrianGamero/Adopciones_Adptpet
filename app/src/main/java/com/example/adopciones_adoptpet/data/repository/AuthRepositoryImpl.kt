package com.example.adopciones_adoptpet.data.repository

import com.example.adopciones_adoptpet.data.dataSource.UserRemoteDataSource
import com.example.adopciones_adoptpet.domain.model.LoggedUserEntity
import com.example.adopciones_adoptpet.domain.model.ShelterExtraData
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
                            try {
                                val user = remoteDataSource.getUser(uid).getOrThrow()
                                sessionManager.saveSession(user)
                                if(user.role.equals("shelter")){
                                    val shelterExtraData = remoteDataSource.getShelterExtra(uid).getOrThrow()
                                    sessionManager.saveExtraData(shelterExtraData)

                                }
                                cont.resume(Result.success(user))
                            } catch (e: Exception) {
                                cont.resume(Result.failure(e))
                            }
                        }
                    },
                    onFailure = {
                        cont.resume(Result.failure(it))
                    }
                )
            }
        }
    }

    override suspend fun signUpUserAdopter(
        name: String,
        email: String,
        password: String,
        phone: Int,
        role:String
    ): Result<Unit> = suspendCancellableCoroutine { cont ->

        FirebaseAuthService.signUp(email, password) { result ->
            result.fold(
                onSuccess = { firebaseUser ->
                    val user = LoggedUserEntity(
                        uid = firebaseUser.uid,
                        name = name,
                        email = email,
                        role = role,
                        phone = phone
                    )

                    CoroutineScope(Dispatchers.IO).launch {
                        val saveResult = remoteDataSource.saveUser(user)
                        cont.resume(saveResult)
                    }

                },
                onFailure = { error ->
                    cont.resume(Result.failure(error))
                }
            )
        }
    }

    override suspend fun signUpUserShelter(
        name: String,
        email: String,
        password: String,
        phone: Int,
        role:String,
        address: String,
        city: String,
        website: String
    ): Result<Unit> = suspendCancellableCoroutine { cont ->

        FirebaseAuthService.signUp(email, password) { result ->
            result.fold(
                onSuccess = { firebaseUser ->
                    val user = LoggedUserEntity(
                        uid = firebaseUser.uid,
                        name = name,
                        email = email,
                        role = role,
                        phone = phone,
                    )
                    val extraData = ShelterExtraData(
                        uid = firebaseUser.uid,
                        address = address,
                        city = city,
                        website = website
                    )

                    CoroutineScope(Dispatchers.IO).launch {
                        val userResult = remoteDataSource.saveUser(user)
                        if (userResult.isFailure) {
                            cont.resume(Result.failure(userResult.exceptionOrNull()!!))
                            return@launch
                        }

                        val extraResult = remoteDataSource.saveShelterExtra(extraData)
                        if (extraResult.isFailure) {
                            cont.resume(Result.failure(extraResult.exceptionOrNull()!!))
                            return@launch
                        }

                        cont.resume(Result.success(Unit))
                    }

                },
                onFailure = { error ->
                    cont.resume(Result.failure(error))
                }
            )
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