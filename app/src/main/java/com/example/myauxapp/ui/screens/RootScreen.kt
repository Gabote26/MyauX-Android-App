package com.example.myauxapp.ui.screens

sealed class RootScreen(val route: String) {
    object Login : RootScreen("login")
    object Main : RootScreen("main")
}
