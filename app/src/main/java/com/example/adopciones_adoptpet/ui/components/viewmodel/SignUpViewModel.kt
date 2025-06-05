package com.example.adopciones_adoptpet.ui.components.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adopciones_adoptpet.domain.useCase.SignUpUserUseCase
import com.example.adopciones_adoptpet.utils.ErrorMessages
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import kotlinx.coroutines.launch

class SignUpViewModel(
    val signUpUserUseCase: SignUpUserUseCase
): ViewModel() {


    var signUpResult by mutableStateOf<Result<Unit>?>(null)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun signUpAdopter(name: String, email: String, password: String, phone: Int, role:String) {
        viewModelScope.launch {
            val result = signUpUserUseCase.signUpAdopter(name, email, password, phone,role)
            handleSignUpResult(result)

        }
    }
    fun signUpShelter(name: String, email: String, password: String, phone: Int, role:String,address: String,city:String,website: String) {
        viewModelScope.launch {
            val result = signUpUserUseCase.signUpShelter(name, email, password, phone,role, address,city,website)
            handleSignUpResult(result)

        }
    }
    fun clearSignUpResult() {
        signUpResult = null
    }

    private fun handleSignUpResult(result: Result<Unit>) {
        result
            .onSuccess {
                signUpResult = Result.success(Unit)
                errorMessage = null
            }
            .onFailure {
                signUpResult = Result.failure(it)
                errorMessage = formatFirebaseAuthError(it)
            }
    }

    private fun formatFirebaseAuthError(exception: Throwable): String {
        return when (exception) {
            is FirebaseAuthUserCollisionException -> ErrorMessages.USER_ALREADY_EXISTS
            is FirebaseAuthWeakPasswordException -> ErrorMessages.WEAK_PASSWORD
            is FirebaseAuthInvalidCredentialsException -> ErrorMessages.INVALID_EMAIL
            is FirebaseAuthException -> ErrorMessages.authError(exception.message)
            else -> ErrorMessages.unknownError(exception.localizedMessage)
        }
    }
}