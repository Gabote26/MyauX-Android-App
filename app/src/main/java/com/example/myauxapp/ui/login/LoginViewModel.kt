package com.example.myauxapp.ui.login

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myauxapp.data.remote.RetrofitClient
import com.example.myauxapp.data.remote.LoginRequest
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val _state = mutableStateOf<LoginState>(LoginState.Idle)
    val state: State<LoginState> = _state

    fun login(email: String, password: String) {
        _state.value = LoginState.Loading

        viewModelScope.launch {
            try {
                // Verificar si es un profesor con credenciales hardcodeadas
                if (email.contains("@profesor.com") || email.contains("profe")) {
                    // Credenciales hardcodeadas para profesores
                    if ((email == "profe1@gmail.com" && password == "profe123") ||
                        (email == "profesor@profesor.com" && password == "profe123")) {
                        _state.value = LoginState.Success(
                            nombre = "Profesor",
                            userRole = "profesor"
                        )
                        return@launch
                    } else {
                        _state.value = LoginState.Error("Credenciales de profesor inválidas")
                        return@launch
                    }
                }

                // Si no es profesor, intentar login normal de estudiante
                val response = RetrofitClient.api.login(
                    LoginRequest(email, password)
                )

                if (response.isSuccessful && response.body() != null) {
                    _state.value = LoginState.Success(
                        nombre = response.body()!!.nombre,
                        userRole = "estudiante"
                    )
                } else {
                    _state.value =
                        LoginState.Error("Credenciales inválidas")
                }

            } catch (e: Exception) {
                _state.value =
                    LoginState.Error("No se pudo conectar al servidor")
            }
        }
    }

    fun retry(email: String, password: String) {
        login(email, password)
    }
}
