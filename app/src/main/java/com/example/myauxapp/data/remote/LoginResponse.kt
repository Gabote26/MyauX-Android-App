package com.example.myauxapp.data.remote

data class LoginResponse(
    val id: Long,
    val nombre: String,
    val email: String,
    val rol: String
)