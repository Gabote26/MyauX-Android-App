package com.example.myauxapp.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myauxapp.ui.components.BottomBar
import com.example.myauxapp.ui.navigation.NavScreen
import com.example.myauxapp.ui.navigation.UserNavigation

@Composable
fun MainScreen() {

    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute in listOf(
        NavScreen.HomeScreen.name,
        NavScreen.Horarios.name,
        NavScreen.Avisos.name,
        NavScreen.Estudiante.name,
        NavScreen.Config.name
    )

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomBar(navController)
            }
        }
    ) { padding ->
        UserNavigation(
            navController = navController,
            modifier = Modifier.padding(padding)
        )
    }
}
