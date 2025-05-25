package com.example.adopciones_adoptpet.ui.components.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adopciones_adoptpet.domain.useCase.SignUpUserUseCase
import kotlinx.coroutines.launch

class SignUpViewModel(
    val signUpUserUseCase: SignUpUserUseCase
): ViewModel() {


    var signUpResult by mutableStateOf<Result<Unit>?>(null)
        private set

    fun signUpAdopter(name: String, email: String, password: String, phone: Int, role:String) {
        viewModelScope.launch {
            signUpResult = signUpUserUseCase.signUpAdopter(name, email, password, phone,role)

        }
    }
    fun signUpShelter(name: String, email: String, password: String, phone: Int, role:String,address: String,city:String,website: String) {
        viewModelScope.launch {
            signUpResult = signUpUserUseCase.signUpShelter(name, email, password, phone,role, address,city,website)
        }
    }
    fun clearSignUpResult() {
        signUpResult = null
    }
}