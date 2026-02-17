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

    Scaffold(
        bottomBar = {
            BottomBar(navController)
        }
    ) { padding ->
        UserNavigation(
            navController = navController,
            modifier = Modifier.padding(padding)
        )
    }
}