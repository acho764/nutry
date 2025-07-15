package com.example.nutry

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.nutry.ui.navigation.BottomNavigation
import com.example.nutry.ui.navigation.NavigationItem
import com.example.nutry.ui.screens.ingredients.IngredientsScreen
import com.example.nutry.ui.screens.dishes.DishesScreen
import com.example.nutry.ui.screens.track.TrackScreen
import com.example.nutry.ui.screens.recommendations.RecommendationsScreen
import com.example.nutry.ui.screens.settings.SettingsScreen
import com.example.nutry.ui.theme.NutryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NutryTheme {
                NutryApp()
            }
        }
    }
}

@Composable
fun NutryApp() {
    val navController = rememberNavController()
    
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomNavigation(navController = navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = NavigationItem.Ingredients.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(NavigationItem.Ingredients.route) {
                IngredientsScreen()
            }
            composable(NavigationItem.Dishes.route) {
                DishesScreen()
            }
            composable(NavigationItem.Track.route) {
                TrackScreen()
            }
            composable(NavigationItem.Recommendations.route) {
                RecommendationsScreen()
            }
            composable(NavigationItem.Settings.route) {
                SettingsScreen()
            }
        }
    }
}