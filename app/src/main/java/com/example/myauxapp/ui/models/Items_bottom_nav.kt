package com.example.myauxapp.ui.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Announcement
import androidx.compose.material.icons.outlined.Announcement
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.VerifiedUser
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.myauxapp.ui.navigation.NavScreen

sealed class Items_bottom_nav (
    val icon: ImageVector,
    val title: String,
    val ruta: String
) {
    object Item_bottom_nav1: Items_bottom_nav(
        Icons.Outlined.Home,
        "Home",
        NavScreen.HomeScreen.name
    )
    object Item_bottom_nav2: Items_bottom_nav(
        Icons.Outlined.CalendarMonth,
        "Horarios",
        NavScreen.Horarios.name
    )
    object Item_bottom_nav3: Items_bottom_nav(
        Icons.AutoMirrored.Outlined.Announcement,
        "Avisos",
        NavScreen.Avisos.name
    )
    object Item_bottom_nav4: Items_bottom_nav(
        Icons.Outlined.VerifiedUser,
        "User",
        NavScreen.Estudiante.name
    )
    object Item_bottom_nav5: Items_bottom_nav(
        Icons.Outlined.Settings,
        "Config",
        NavScreen.Config.name
    )
}