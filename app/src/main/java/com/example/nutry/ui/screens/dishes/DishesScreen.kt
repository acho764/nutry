package com.example.nutry.ui.screens.dishes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nutry.NutryApplication
import com.example.nutry.data.entities.Dish
import com.example.nutry.ui.components.DishDialog
import com.example.nutry.ui.components.DishItem
import com.example.nutry.ui.viewmodels.DishViewModel
import com.example.nutry.ui.viewmodels.DishViewModelFactory
import com.example.nutry.ui.viewmodels.IngredientViewModel
import com.example.nutry.ui.viewmodels.IngredientViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DishesScreen() {
    val context = LocalContext.current
    val application = context.applicationContext as NutryApplication
    val dishViewModel: DishViewModel = viewModel(
        factory = DishViewModelFactory(application.dishRepository)
    )
    val ingredientViewModel: IngredientViewModel = viewModel(
        factory = IngredientViewModelFactory(application.ingredientRepository)
    )
    
    val dishes by dishViewModel.dishes.collectAsState()
    val dishIngredients by dishViewModel.dishIngredients.collectAsState()
    val ingredients by ingredientViewModel.ingredientsWithCategory.collectAsState()
    val isLoading by dishViewModel.isLoading.collectAsState()
    val error by dishViewModel.error.collectAsState()
    
    var showAddDialog by remember { mutableStateOf(false) }
    var editingDish by remember { mutableStateOf<Dish?>(null) }
    
    // Force refresh after operations
    LaunchedEffect(dishes.size) {
        // This will trigger when dishes list changes
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "🍽️ Dishes",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            
            FloatingActionButton(
                onClick = { showAddDialog = true },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Dish"
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
            items(dishes) { dish ->
                val dishIngredientsList = dishIngredients[dish.id] ?: emptyList()
                DishItem(
                    dish = dish,
                    ingredients = dishIngredientsList,
                    onEdit = { editingDish = it },
                    onDelete = { dishViewModel.deleteDish(it) }
                )
            }
        }
    }
    
    if (showAddDialog) {
        DishDialog(
            ingredients = ingredients,
            onDismiss = { showAddDialog = false },
            onSave = { name, emoji, ingredientIds ->
                dishViewModel.insertDish(name, emoji, ingredientIds)
                showAddDialog = false
            }
        )
    }
    
    editingDish?.let { dish ->
        val currentIngredientIds = dishIngredients[dish.id]?.map { it.id } ?: emptyList()
        DishDialog(
            dish = dish,
            ingredients = ingredients,
            selectedIngredientIds = currentIngredientIds,
            onDismiss = { editingDish = null },
            onSave = { name, emoji, ingredientIds ->
                dishViewModel.updateDish(dish.copy(name = name, emoji = emoji), ingredientIds)
                editingDish = null
            },
            onDelete = { dishToDelete ->
                dishViewModel.deleteDish(dishToDelete)
                editingDish = null
            }
        )
    }
}