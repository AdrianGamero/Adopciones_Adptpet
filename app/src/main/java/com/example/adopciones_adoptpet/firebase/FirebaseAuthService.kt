package com.example.adopciones_adoptpet.firebase

import android.widget.Toast
import com.example.adopciones_adoptpet.domain.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

object FirebaseAuthService {
    private val auth = FirebaseAuth.getInstance()
    fun currentUser(): FirebaseUser? = auth.currentUser

    fun logIn(email: String, password: String, onResult: (Result<FirebaseUser>) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { onResult(Result.success(it.user!!)) }
            .addOnFailureListener { onResult(Result.failure(it)) }
    }

    fun logOut() {
        auth.signOut()
    }
}