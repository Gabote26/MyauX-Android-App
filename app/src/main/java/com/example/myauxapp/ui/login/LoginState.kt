package com.example.myauxapp.ui.login

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val nombre: String) : LoginState()
    data class Error(val message: String) : LoginState()
}
