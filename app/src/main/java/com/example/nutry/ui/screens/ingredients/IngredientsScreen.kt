package com.example.nutry.ui.screens.ingredients

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
import com.example.nutry.data.entities.Category
import com.example.nutry.data.entities.Ingredient
import com.example.nutry.ui.components.CategoryDialog
import com.example.nutry.ui.components.CategoryItem
import com.example.nutry.ui.components.IngredientDialog
import com.example.nutry.ui.components.IngredientItem
import com.example.nutry.ui.viewmodels.CategoryViewModel
import com.example.nutry.ui.viewmodels.CategoryViewModelFactory
import com.example.nutry.ui.viewmodels.IngredientViewModel
import com.example.nutry.ui.viewmodels.IngredientViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IngredientsScreen() {
    val context = LocalContext.current
    val application = context.applicationContext as NutryApplication
    val categoryViewModel: CategoryViewModel = viewModel(
        factory = CategoryViewModelFactory(application.categoryRepository)
    )
    val ingredientViewModel: IngredientViewModel = viewModel(
        factory = IngredientViewModelFactory(application.ingredientRepository)
    )
    
    val categories by categoryViewModel.categories.collectAsState()
    val ingredients by ingredientViewModel.ingredients.collectAsState()
    val isLoading by categoryViewModel.isLoading.collectAsState()
    val error by categoryViewModel.error.collectAsState()
    
    var showAddCategoryDialog by remember { mutableStateOf(false) }
    var showAddIngredientDialog by remember { mutableStateOf(false) }
    var editingCategory by remember { mutableStateOf<Category?>(null) }
    var editingIngredient by remember { mutableStateOf<Ingredient?>(null) }
    
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
                text = "🥬 Ingredients",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            
            Row {
                FloatingActionButton(
                    onClick = { showAddIngredientDialog = true },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Ingredient"
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                FloatingActionButton(
                    onClick = { showAddCategoryDialog = true },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Category"
                    )
                }
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
            categories.forEach { category ->
                item {
                    CategoryItem(
                        category = category,
                        onEdit = { editingCategory = it },
                        onDelete = { categoryViewModel.deleteCategory(it) }
                    )
                }
                
                val categoryIngredients = ingredients.filter { it.categoryId == category.id }
                items(categoryIngredients) { ingredient ->
                    IngredientItem(
                        ingredient = ingredient,
                        category = category,
                        onEdit = { editingIngredient = it },
                        onDelete = { ingredientViewModel.deleteIngredient(it) }
                    )
                }
            }
        }
    }
    
    if (showAddCategoryDialog) {
        CategoryDialog(
            onDismiss = { showAddCategoryDialog = false },
            onSave = { name, emoji ->
                categoryViewModel.insertCategory(name, emoji)
                showAddCategoryDialog = false
            }
        )
    }
    
    if (showAddIngredientDialog) {
        IngredientDialog(
            categories = categories,
            onDismiss = { showAddIngredientDialog = false },
            onSave = { name, categoryId ->
                ingredientViewModel.insertIngredient(name, categoryId)
                showAddIngredientDialog = false
            }
        )
    }
    
    editingCategory?.let { category ->
        CategoryDialog(
            category = category,
            onDismiss = { editingCategory = null },
            onSave = { name, emoji ->
                categoryViewModel.updateCategory(category.copy(name = name, emoji = emoji))
                editingCategory = null
            }
        )
    }
    
    editingIngredient?.let { ingredient ->
        IngredientDialog(
            ingredient = ingredient,
            categories = categories,
            onDismiss = { editingIngredient = null },
            onSave = { name, categoryId ->
                ingredientViewModel.updateIngredient(ingredient.copy(name = name, categoryId = categoryId))
                editingIngredient = null
            }
        )
    }
}