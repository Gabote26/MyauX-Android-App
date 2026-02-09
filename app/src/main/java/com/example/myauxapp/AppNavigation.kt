package com.example.myauxapp

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import com.example.myauxapp.ui.screens.*
import com.example.myauxapp.ui.login.LoginScreen
import com.example.myauxapp.ui.screens.RootScreen

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = RootScreen.Login.route,

        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(350)
            ) + fadeIn()
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { -it / 3 },
                animationSpec = tween(300)
            ) + fadeOut()
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { -it },
                animationSpec = tween(350)
            ) + fadeIn()
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(300)
            ) + fadeOut()
        }
    ) {

        composable(RootScreen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(RootScreen.Main.route) {
                        popUpTo(RootScreen.Login.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(RootScreen.Main.route) {
            MainScreen()
        }
    }
}
