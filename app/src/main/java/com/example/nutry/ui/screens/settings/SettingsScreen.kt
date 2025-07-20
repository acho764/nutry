package com.example.nutry.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nutry.NutryApplication
import com.example.nutry.ui.viewmodels.SettingsViewModel
import com.example.nutry.ui.viewmodels.SettingsViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    val context = LocalContext.current
    val application = context.applicationContext as NutryApplication
    val viewModel: SettingsViewModel = viewModel(
        factory = SettingsViewModelFactory(application.settingsRepository)
    )
    
    val settings by viewModel.settings.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    
    var ingredientTimewindow by remember { mutableStateOf(7f) }
    var dishTimewindow by remember { mutableStateOf(14f) }
    var excludeSpices by remember { mutableStateOf(false) }
    
    // Update local state when settings change
    LaunchedEffect(settings) {
        ingredientTimewindow = settings.ingredientBasedTimewindow.toFloat()
        dishTimewindow = settings.dishBasedTimewindow.toFloat()
        excludeSpices = settings.excludeSpices
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "⚙️ Settings",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
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
                    .padding(bottom = 16.dp),
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
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Freshness Timewindows",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                
                // Ingredient timewindow slider
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Ingredients",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.weight(0.3f)
                    )
                    
                    Slider(
                        value = ingredientTimewindow,
                        onValueChange = { newValue ->
                            ingredientTimewindow = newValue
                        },
                        onValueChangeFinished = {
                            val newSettings = settings.copy(
                                ingredientBasedTimewindow = ingredientTimewindow.toInt()
                            )
                            viewModel.updateSettings(newSettings)
                        },
                        valueRange = 1f..30f,
                        steps = 28,
                        modifier = Modifier
                            .weight(0.5f)
                            .height(20.dp),
                        colors = SliderDefaults.colors(
                            thumbColor = androidx.compose.ui.graphics.Color(0xFF4CAF50),
                            activeTrackColor = androidx.compose.ui.graphics.Color(0xFF4CAF50),
                            inactiveTrackColor = androidx.compose.ui.graphics.Color(0xFF9E9E9E),
                            activeTickColor = androidx.compose.ui.graphics.Color.White,
                            inactiveTickColor = androidx.compose.ui.graphics.Color.White
                        )
                    )
                    
                    Text(
                        text = "${ingredientTimewindow.toInt()}d",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(0.2f)
                    )
                }
                
                // Dish timewindow slider
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Dishes",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.weight(0.3f)
                    )
                    
                    Slider(
                        value = dishTimewindow,
                        onValueChange = { newValue ->
                            dishTimewindow = newValue
                        },
                        onValueChangeFinished = {
                            val newSettings = settings.copy(
                                dishBasedTimewindow = dishTimewindow.toInt()
                            )
                            viewModel.updateSettings(newSettings)
                        },
                        valueRange = 1f..30f,
                        steps = 28,
                        modifier = Modifier
                            .weight(0.5f)
                            .height(20.dp),
                        colors = SliderDefaults.colors(
                            thumbColor = androidx.compose.ui.graphics.Color(0xFF4CAF50),
                            activeTrackColor = androidx.compose.ui.graphics.Color(0xFF4CAF50),
                            inactiveTrackColor = androidx.compose.ui.graphics.Color(0xFF9E9E9E),
                            activeTickColor = androidx.compose.ui.graphics.Color.White,
                            inactiveTickColor = androidx.compose.ui.graphics.Color.White
                        )
                    )
                    
                    Text(
                        text = "${dishTimewindow.toInt()}d",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(0.2f)
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Exclude Spices toggle
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Exclude Spices from Freshness",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "🧂 Подправки category won't affect freshness scores",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    
                    Switch(
                        checked = excludeSpices,
                        onCheckedChange = { newValue ->
                            excludeSpices = newValue
                            val newSettings = settings.copy(excludeSpices = newValue)
                            viewModel.updateSettings(newSettings)
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = androidx.compose.ui.graphics.Color(0xFF4CAF50),
                            checkedTrackColor = androidx.compose.ui.graphics.Color(0xFF4CAF50).copy(alpha = 0.5f)
                        )
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "How it works",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                
                Text(
                    text = "• Freshness starts at 0% right after consumption",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Text(
                    text = "• It gradually increases to 100% over the configured timewindow",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Text(
                    text = "• Items with higher freshness scores are recommended first",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Text(
                    text = "• Use shorter timewindows for more frequent recommendations",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}