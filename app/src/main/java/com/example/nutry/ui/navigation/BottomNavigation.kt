package com.example.nutry.ui.navigation

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigation(
    navController: NavController
) {
    val items = listOf(
        NavigationItem.Ingredients,
        NavigationItem.Dishes,
        NavigationItem.Track,
        NavigationItem.Recommendations,
        NavigationItem.Settings
    )
    
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    
    NavigationBar(
        modifier = Modifier.height(115.dp)
    ) {
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { 
                    Text(
                        text = item.title,
                        fontWeight = if (currentRoute == item.route) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 10.sp,
                        maxLines = 1
                    )
                },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}