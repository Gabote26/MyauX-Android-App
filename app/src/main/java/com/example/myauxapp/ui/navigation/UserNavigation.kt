package com.example.myauxapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.myauxapp.ui.screens.Avisos
import com.example.myauxapp.ui.screens.Config
import com.example.myauxapp.ui.screens.Estudiante
import com.example.myauxapp.ui.screens.HomeScreen
import com.example.myauxapp.ui.screens.Horarios

@Composable
fun UserNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = NavScreen.HomeScreen.name,
        modifier = modifier
    ) {
        composable(NavScreen.HomeScreen.name) { HomeScreen() }
        composable(NavScreen.Horarios.name) { Horarios() }
        composable(NavScreen.Avisos.name) { Avisos() }
        composable(NavScreen.Estudiante.name) { Estudiante() }
        composable(NavScreen.Config.name) { Config() }
    }
}