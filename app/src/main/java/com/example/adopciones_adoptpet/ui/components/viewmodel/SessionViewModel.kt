package com.example.adopciones_adoptpet.ui.components.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adopciones_adoptpet.domain.model.LoggedUserEntity
import com.example.adopciones_adoptpet.utils.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SessionViewModel(
    private val sessionManager: SessionManager
) : ViewModel() {


    private val _loggedUser = MutableStateFlow<LoggedUserEntity?>(null)
    val loggedUser: StateFlow<LoggedUserEntity?> = _loggedUser

    init {
        viewModelScope.launch {
            _loggedUser.value = sessionManager.getSession()
        }
    }

    fun clearSession() {
        viewModelScope.launch {
            sessionManager.clearSession()
            _loggedUser.value = null
        }
    }
}