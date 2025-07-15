package com.example.nutry.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.nutry.data.entities.Dish
import com.example.nutry.data.entities.Ingredient

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DishDialog(
    dish: Dish? = null,
    ingredients: List<Ingredient>,
    selectedIngredientIds: List<Int> = emptyList(),
    onDismiss: () -> Unit,
    onSave: (String, String, List<Int>) -> Unit
) {
    var name by remember { mutableStateOf(dish?.name ?: "") }
    var emoji by remember { mutableStateOf(dish?.emoji ?: "") }
    var selectedIds by remember { mutableStateOf(selectedIngredientIds.toSet()) }
    
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f)
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = if (dish == null) "Add Dish" else "Edit Dish",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                
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
                
                Text(
                    text = "Select Ingredients:",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .selectableGroup(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(ingredients) { ingredient ->
                        val isSelected = selectedIds.contains(ingredient.id)
                        Row(
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
                                )
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = isSelected,
                                onCheckedChange = null
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = ingredient.name,
                                fontSize = 14.sp,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
                
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