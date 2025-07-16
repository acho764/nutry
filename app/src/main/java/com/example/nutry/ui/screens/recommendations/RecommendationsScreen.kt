package com.example.nutry.ui.screens.recommendations

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nutry.NutryApplication
import com.example.nutry.ui.components.RecommendationItemCard
import com.example.nutry.ui.viewmodels.RecommendationsViewModel
import com.example.nutry.ui.viewmodels.RecommendationsViewModelFactory
import com.example.nutry.ui.viewmodels.RecommendationType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecommendationsScreen() {
    val context = LocalContext.current
    val application = context.applicationContext as NutryApplication
    val viewModel: RecommendationsViewModel = viewModel(
        factory = RecommendationsViewModelFactory(
            application.dishRepository,
            application.ingredientRepository,
            application.trackRepository,
            application.settingsRepository
        )
    )
    
    val recommendations by viewModel.recommendations.collectAsState()
    val recommendationMode by viewModel.recommendationMode.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "💡 Recommendations",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        Text(
            text = "Recommendation Mode:",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        Text(
            text = if (recommendationMode == RecommendationType.DISH_BASED) "Dish-based (freshness based on dish consumption)" else "Ingredient-based (average freshness of dish ingredients)",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Dish-based",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Ingredient-based",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Slider(
                    value = if (recommendationMode == RecommendationType.DISH_BASED) 0f else 1f,
                    onValueChange = { value ->
                        val newMode = if (value < 0.5f) RecommendationType.DISH_BASED else RecommendationType.INGREDIENT_BASED
                        viewModel.setRecommendationMode(newMode)
                    },
                    valueRange = 0f..1f,
                    steps = 0,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        
        error?.let { errorMessage ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
        
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (recommendations.isEmpty() && !isLoading) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "No recommendations available",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "Add some ingredients and dishes to get personalized recommendations!",
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }
            } else {
                items(recommendations) { recommendation ->
                    RecommendationItemCard(
                        recommendation = recommendation,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}