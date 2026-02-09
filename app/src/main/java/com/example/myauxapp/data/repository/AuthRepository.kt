package com.example.myauxapp.data.repository

import com.example.myauxapp.data.remote.LoginRequest
import com.example.myauxapp.data.remote.RetrofitClient

class AuthRepository {

    suspend fun login(email: String, password: String) = RetrofitClient.api.login(
        LoginRequest(email, password)
    )
}