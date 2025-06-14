package com.example.adopciones_adoptpet.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

object FirebaseAuthService {
    private val auth = FirebaseAuth.getInstance()
    fun currentUser(): FirebaseUser? = auth.currentUser

    fun logIn(email: String, password: String, onResult: (Result<FirebaseUser>) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                onResult(Result.success(it.user!!))
            }
            .addOnFailureListener { onResult(Result.failure(it)) }
    }

    fun signUp(email: String, password: String, onResult: (Result<FirebaseUser>) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                onResult(Result.success(it.user!!))
                logOut()
            }
            .addOnFailureListener { onResult(Result.failure(it)) }
    }

    fun logOut() {
        auth.signOut()
    }
}