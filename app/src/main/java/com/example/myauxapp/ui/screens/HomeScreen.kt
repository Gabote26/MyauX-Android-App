package com.example.myauxapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import com.example.myauxapp.ui.components.BottomBar
import com.example.myauxapp.ui.navigation.UserNavigation

@Composable
fun HomeScreen() {
    Box(
        modifier = Modifier.fillMaxSize().background(Color(0xFFF7F7F7)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Bienvenido a MyauX",
            style = MaterialTheme.typography.displayMedium,
            color = Color(0xFF4A148C)
        )
    }
}
