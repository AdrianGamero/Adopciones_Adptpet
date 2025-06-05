package com.example.adopciones_adoptpet.ui.components.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adopciones_adoptpet.domain.model.LoggedUserEntity
import com.example.adopciones_adoptpet.domain.model.ShelterExtraData
import com.example.adopciones_adoptpet.domain.useCase.LogInUseCase
import com.example.adopciones_adoptpet.ui.components.logIn.LoginUiState
import com.example.adopciones_adoptpet.utils.ErrorMessages
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SessionViewModel(
    private val logInUseCase: LogInUseCase
) : ViewModel() {


    private val _loggedUser = MutableStateFlow<LoggedUserEntity?>(null)
    val loggedUser: StateFlow<LoggedUserEntity?> = _loggedUser

    private val _loginState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val loginState: StateFlow<LoginUiState> = _loginState

    private val _shelterExtraData = MutableStateFlow<ShelterExtraData?>(null)
    val shelterExtraData: StateFlow<ShelterExtraData?> = _shelterExtraData


    init {
        viewModelScope.launch {
            _loggedUser.value = logInUseCase.getSession()
        }
    }

    fun clearSession() {
        viewModelScope.launch {
            logInUseCase.clearSession()
            _loggedUser.value = null
            _shelterExtraData.value = null
            _loginState.value = LoginUiState.Idle
        }
    }

    fun logIn(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginUiState.Loading

            val result = logInUseCase.logIn(email, password)

            result.fold(
                onSuccess = { userWithExtra ->
                    _loggedUser.value = userWithExtra.user
                    _shelterExtraData.value = userWithExtra.shelterExtraData

                    _loginState.value = LoginUiState.Success

                },
                onFailure = {

                    val message = mapFirebaseAuthExceptionToMessage(it)
                    _loginState.value = LoginUiState.Error(message)
                }
            )
        }
    }

    fun mapFirebaseAuthExceptionToMessage(exception: Throwable): String {
        return when (exception) {
            is FirebaseAuthInvalidCredentialsException ->ErrorMessages.INVALID_CREDENTIALS
            is FirebaseNetworkException -> ErrorMessages.NETWORK_ERROR
            else -> ErrorMessages.UNKNOWN_ERROR
        }
    }
}