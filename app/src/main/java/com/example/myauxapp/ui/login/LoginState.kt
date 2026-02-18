package com.example.myauxapp.ui.login

sealed class LoginState {
    data object Idle : LoginState()
    data object Loading : LoginState()
    data class Success(val nombre: String, val userRole: String = "estudiante") : LoginState()
    data class Error(val message: String) : LoginState()
}
