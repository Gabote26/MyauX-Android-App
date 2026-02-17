package com.example.myauxapp.ui.components

import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.myauxapp.ui.models.Items_bottom_nav.*
import com.example.myauxapp.ui.navigation.currentRoute

@Composable
fun BottomBar(
    navController: NavHostController
) {
    val menu_items = listOf(
        Item_bottom_nav1,
        Item_bottom_nav2,
        Item_bottom_nav3,
        Item_bottom_nav4,
        Item_bottom_nav5
    )

        NavigationBar(
            containerColor = MaterialTheme.colorScheme.inverseOnSurface
        ) {
            menu_items.forEach {item->
                val selected = currentRoute(navController) == item.ruta
                NavigationBarItem(
                    selected = selected,
                    onClick = {
                        navController.navigate(item.ruta) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = {
                        Icon(imageVector = item.icon,
                            contentDescription = item.title
                        )
                    },
                    label = {
                        Text(text = item.title)
                    }
                )
            }
        }

}