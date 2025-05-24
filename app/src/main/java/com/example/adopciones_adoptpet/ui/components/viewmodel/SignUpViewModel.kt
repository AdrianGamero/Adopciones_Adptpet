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

    fun signUp(name: String, email: String, password: String, phone: Int) {
        viewModelScope.launch {
            signUpResult = signUpUserUseCase(name, email, password, phone)
        }
    }
}