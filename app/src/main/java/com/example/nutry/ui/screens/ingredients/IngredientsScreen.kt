package com.example.nutry.ui.screens.ingredients

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nutry.NutryApplication
import com.example.nutry.data.entities.Category
import com.example.nutry.data.entities.Ingredient
import com.example.nutry.data.entities.Dish
import com.example.nutry.ui.components.CategoryDialog
import com.example.nutry.ui.components.CategoryItem
import com.example.nutry.ui.components.IngredientDialog
import com.example.nutry.ui.components.IngredientItem
import com.example.nutry.ui.components.TrackDialog
import com.example.nutry.ui.viewmodels.CategoryViewModel
import com.example.nutry.ui.viewmodels.CategoryViewModelFactory
import com.example.nutry.ui.viewmodels.IngredientViewModel
import com.example.nutry.ui.viewmodels.IngredientViewModelFactory
import com.example.nutry.ui.viewmodels.TrackViewModel
import com.example.nutry.ui.viewmodels.TrackViewModelFactory
import com.example.nutry.ui.viewmodels.DishViewModel
import com.example.nutry.ui.viewmodels.DishViewModelFactory
import com.example.nutry.ui.viewmodels.SettingsViewModel
import com.example.nutry.ui.viewmodels.SettingsViewModelFactory
import com.example.nutry.utils.FreshnessCalculator
import java.util.Date

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
    val trackViewModel: TrackViewModel = viewModel(
        factory = TrackViewModelFactory(application.trackRepository)
    )
    val dishViewModel: DishViewModel = viewModel(
        factory = DishViewModelFactory(application.dishRepository)
    )
    val settingsViewModel: SettingsViewModel = viewModel(
        factory = SettingsViewModelFactory(application.settingsRepository)
    )
    
    val categories by categoryViewModel.categories.collectAsState()
    val ingredients by ingredientViewModel.ingredients.collectAsState()
    val dishes by dishViewModel.dishes.collectAsState()
    val trackEntries by trackViewModel.trackEntries.collectAsState()
    val settings by settingsViewModel.settings.collectAsState()
    val isLoading by categoryViewModel.isLoading.collectAsState()
    val error by categoryViewModel.error.collectAsState()
    
    var showAddCategoryDialog by remember { mutableStateOf(false) }
    var showAddIngredientDialog by remember { mutableStateOf(false) }
    var showAddChoiceDialog by remember { mutableStateOf(false) }
    var showEatConfirmation by remember { mutableStateOf(false) }
    var editingCategory by remember { mutableStateOf<Category?>(null) }
    var editingIngredient by remember { mutableStateOf<Ingredient?>(null) }
    var eatingIngredient by remember { mutableStateOf<Ingredient?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var showSearch by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    
    // Filter categories and ingredients based on search query
    val filteredCategories = remember(categories, ingredients, searchQuery) {
        if (searchQuery.isBlank()) {
            categories
        } else {
            categories.filter { category ->
                category.name.contains(searchQuery, ignoreCase = true) ||
                ingredients.any { ingredient ->
                    ingredient.categoryId == category.id &&
                    ingredient.name.contains(searchQuery, ignoreCase = true)
                }
            }
        }
    }
    
    val filteredIngredients = remember(ingredients, searchQuery) {
        if (searchQuery.isBlank()) {
            ingredients
        } else {
            ingredients.filter { ingredient ->
                ingredient.name.contains(searchQuery, ignoreCase = true)
            }
        }
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
            if (selectedCategory != null) {
                // Show back button and category name when viewing category ingredients
                if (!showSearch) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { selectedCategory = null }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                        Text(
                            text = "${selectedCategory!!.emoji} ${selectedCategory!!.name}",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { selectedCategory = null }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Search ingredients...") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search"
                                )
                            },
                            trailingIcon = if (searchQuery.isNotEmpty()) {
                                {
                                    IconButton(onClick = { searchQuery = "" }) {
                                        Icon(
                                            imageVector = Icons.Default.Clear,
                                            contentDescription = "Clear"
                                        )
                                    }
                                }
                            } else null,
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(16.dp)
                        )
                    }
                }
            } else {
                // Show main header and search when viewing categories
                if (!showSearch) {
                    Text(
                        text = "🥬 Ingredients",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search categories...") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search"
                            )
                        },
                        trailingIcon = if (searchQuery.isNotEmpty()) {
                            {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Clear"
                                    )
                                }
                            }
                        } else null,
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp)
                    )
                }
                
                Row {
                    IconButton(
                        onClick = { 
                            showSearch = !showSearch
                            if (!showSearch) searchQuery = ""
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Toggle Search"
                        )
                    }
                    
                    FloatingActionButton(
                        onClick = { showAddChoiceDialog = true },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add"
                        )
                    }
                }
            }
            
            if (selectedCategory != null) {
                // Add search and action buttons for ingredient view
                Row {
                    IconButton(
                        onClick = { 
                            showSearch = !showSearch
                            if (!showSearch) searchQuery = ""
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Toggle Search"
                        )
                    }
                    
                    FloatingActionButton(
                        onClick = { showAddChoiceDialog = true },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add"
                        )
                    }
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
            if (selectedCategory == null) {
                // Show categories only
                if (filteredCategories.isEmpty() && searchQuery.isNotBlank()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Text(
                                text = "No categories found for \"$searchQuery\"",
                                modifier = Modifier.padding(16.dp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    items(filteredCategories) { category ->
                        CategoryItem(
                            category = category,
                            onEdit = { editingCategory = it },
                            onDelete = { categoryViewModel.deleteCategory(it) },
                            onClick = { selectedCategory = it }
                        )
                    }
                }
            } else {
                // Show ingredients for selected category
                val categoryIngredients = ingredients.filter { ingredient ->
                    ingredient.categoryId == selectedCategory!!.id &&
                    (searchQuery.isBlank() || ingredient.name.contains(searchQuery, ignoreCase = true))
                }
                if (categoryIngredients.isEmpty() && searchQuery.isNotBlank()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Text(
                                text = "No ingredients found for \"$searchQuery\"",
                                modifier = Modifier.padding(16.dp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else if (categoryIngredients.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Text(
                                text = "No ingredients in this category",
                                modifier = Modifier.padding(16.dp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    items(categoryIngredients) { ingredient ->
                        val freshnessScore = FreshnessCalculator.calculateIngredientFreshness(
                            ingredient, 
                            trackEntries,
                            settings?.ingredientBasedTimewindow ?: 7, 
                            selectedCategory!!, 
                            settings?.excludeSpices ?: false
                        )
                        IngredientItem(
                            ingredient = ingredient,
                            category = selectedCategory!!,
                            freshnessScore = freshnessScore,
                            onEdit = { editingIngredient = it },
                            onDelete = { ingredientViewModel.deleteIngredient(it) },
                            onEat = { 
                                eatingIngredient = it
                                showEatConfirmation = true
                            },
                            onClick = { editingIngredient = it }
                        )
                    }
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
            onSave = { name, categoryId, emoji ->
                ingredientViewModel.insertIngredient(name, categoryId, emoji)
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
            },
            onDelete = { categoryToDelete ->
                categoryViewModel.deleteCategory(categoryToDelete)
                editingCategory = null
            }
        )
    }
    
    editingIngredient?.let { ingredient ->
        IngredientDialog(
            ingredient = ingredient,
            categories = categories,
            onDismiss = { editingIngredient = null },
            onSave = { name, categoryId, emoji ->
                ingredientViewModel.updateIngredient(ingredient.copy(name = name, categoryId = categoryId, emoji = emoji))
                editingIngredient = null
            },
            onDelete = { ingredientToDelete ->
                ingredientViewModel.deleteIngredient(ingredientToDelete)
                editingIngredient = null
            }
        )
    }
    
    if (showAddChoiceDialog) {
        AlertDialog(
            onDismissRequest = { showAddChoiceDialog = false },
            title = { Text("Add New Item") },
            text = { Text("What would you like to add?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showAddChoiceDialog = false
                        showAddIngredientDialog = true
                    }
                ) {
                    Text("Add Ingredient")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showAddChoiceDialog = false
                        showAddCategoryDialog = true
                    }
                ) {
                    Text("Add Category")
                }
            }
        )
    }
    
    if (showEatConfirmation) {
        eatingIngredient?.let { ingredient ->
            AlertDialog(
                onDismissRequest = { 
                    showEatConfirmation = false
                    eatingIngredient = null
                },
                title = { Text("Track Consumption") },
                text = { 
                    Text("Track consumption of ${ingredient.emoji} ${ingredient.name}?")
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            trackViewModel.insertTrackEntry(null, ingredient.id, 1.0)
                            showEatConfirmation = false
                            eatingIngredient = null
                        }
                    ) {
                        Text("Track")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showEatConfirmation = false
                            eatingIngredient = null
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}