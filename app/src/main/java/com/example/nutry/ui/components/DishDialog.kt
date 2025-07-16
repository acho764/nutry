package com.example.nutry.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.nutry.data.entities.Dish
import com.example.nutry.data.entities.IngredientWithCategory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DishDialog(
    dish: Dish? = null,
    ingredients: List<IngredientWithCategory>,
    selectedIngredientIds: List<Int> = emptyList(),
    onDismiss: () -> Unit,
    onSave: (String, String, List<Int>) -> Unit
) {
    var name by remember { mutableStateOf(dish?.name ?: "") }
    var emoji by remember { mutableStateOf(dish?.emoji ?: "") }
    var selectedIds by remember { mutableStateOf(selectedIngredientIds.toSet()) }
    var searchQuery by remember { mutableStateOf("") }
    
    // Filter ingredients based on search query
    val filteredIngredients = remember(ingredients, searchQuery) {
        if (searchQuery.isBlank()) {
            ingredients
        } else {
            ingredients.filter { 
                it.name.contains(searchQuery, ignoreCase = true) 
            }
        }
    }
    
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Top bar with title and close button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (dish == null) "Add Dish" else "Edit Dish",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
                
                // Dish name and emoji fields
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Dish Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                OutlinedTextField(
                    value = emoji,
                    onValueChange = { emoji = it },
                    label = { Text("Emoji") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                // Ingredients section with search
                Text(
                    text = "Select Ingredients:",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
                
                // Search field
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Search ingredients...") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search"
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                
                // Selected ingredients count
                Text(
                    text = "${selectedIds.size} ingredients selected",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                // Ingredients list
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .selectableGroup(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(filteredIngredients) { ingredient ->
                        val isSelected = selectedIds.contains(ingredient.id)
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = isSelected,
                                    onClick = {
                                        selectedIds = if (isSelected) {
                                            selectedIds - ingredient.id
                                        } else {
                                            selectedIds + ingredient.id
                                        }
                                    },
                                    role = Role.Checkbox
                                ),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = if (isSelected) 4.dp else 1.dp
                            ),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) {
                                    MaterialTheme.colorScheme.primaryContainer
                                } else {
                                    MaterialTheme.colorScheme.surface
                                }
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = isSelected,
                                    onCheckedChange = null
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = if (ingredient.emoji.isNotBlank()) "${ingredient.emoji} ${ingredient.name}" else ingredient.name,
                                        fontSize = 16.sp,
                                        color = if (isSelected) {
                                            MaterialTheme.colorScheme.onPrimaryContainer
                                        } else {
                                            MaterialTheme.colorScheme.onSurface
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
                
                // Bottom action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (name.isNotBlank() && emoji.isNotBlank()) {
                                onSave(name, emoji, selectedIds.toList())
                            }
                        },
                        enabled = name.isNotBlank() && emoji.isNotBlank()
                    ) {
                        Text("Save")
                    }
                }
            }
        }
    }
}