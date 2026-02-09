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
                val response = RetrofitClient.api.login(
                    LoginRequest(email, password)
                )

                if (response.isSuccessful && response.body() != null) {
                    _state.value = LoginState.Success(
                        response.body()!!.nombre
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
