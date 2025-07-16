package com.example.nutry.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.Eco
import androidx.compose.material.icons.outlined.LocalPizza
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavigationItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Ingredients : NavigationItem(
        route = "ingredients",
        title = "Ingredients",
        icon = Icons.Outlined.Eco
    )
    
    object Dishes : NavigationItem(
        route = "dishes", 
        title = "Dishes",
        icon = Icons.Outlined.LocalPizza
    )
    
    object Track : NavigationItem(
        route = "track",
        title = "Track", 
        icon = Icons.Outlined.Analytics
    )
    
    object Recommendations : NavigationItem(
        route = "recommendations",
        title = "Ideas",
        icon = Icons.Outlined.Lightbulb
    )
    
    object Settings : NavigationItem(
        route = "settings",
        title = "Settings",
        icon = Icons.Default.Settings
    )
}