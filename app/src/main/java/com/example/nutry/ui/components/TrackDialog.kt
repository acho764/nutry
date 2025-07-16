package com.example.nutry.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.foundation.text.KeyboardOptions
import com.example.nutry.data.entities.Dish
import com.example.nutry.data.entities.Ingredient
import com.example.nutry.data.entities.TrackEntry
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackDialog(
    trackEntry: TrackEntry? = null,
    dishes: List<Dish>,
    ingredients: List<Ingredient>,
    onDismiss: () -> Unit,
    onSave: (Int?, Int?, Double, Date) -> Unit
) {
    var selectedType by remember { mutableStateOf(
        when {
            trackEntry?.dishId != null -> "dish"
            trackEntry?.ingredientId != null -> "ingredient"
            else -> "dish"
        }
    )}
    var selectedDishId by remember { mutableStateOf(trackEntry?.dishId ?: dishes.firstOrNull()?.id ?: 0) }
    var selectedIngredientId by remember { mutableStateOf(trackEntry?.ingredientId ?: ingredients.firstOrNull()?.id ?: 0) }
    var quantity by remember { mutableStateOf(trackEntry?.quantity?.toString() ?: "1.0") }
    var selectedDate by remember { mutableStateOf(trackEntry?.consumedAt ?: Date()) }
    
    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = if (trackEntry == null) "Add Track Entry" else "Edit Track Entry",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                
                Text(
                    text = "Track Type:",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectableGroup(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        modifier = Modifier.selectable(
                            selected = selectedType == "dish",
                            onClick = { selectedType = "dish" },
                            role = Role.RadioButton
                        ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedType == "dish",
                            onClick = null
                        )
                        Text("Dish", modifier = Modifier.padding(start = 8.dp))
                    }
                    
                    Row(
                        modifier = Modifier.selectable(
                            selected = selectedType == "ingredient",
                            onClick = { selectedType = "ingredient" },
                            role = Role.RadioButton
                        ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedType == "ingredient",
                            onClick = null
                        )
                        Text("Ingredient", modifier = Modifier.padding(start = 8.dp))
                    }
                }
                
                if (selectedType == "dish") {
                    var dishExpanded by remember { mutableStateOf(false) }
                    val selectedDish = dishes.find { it.id == selectedDishId }
                    
                    ExposedDropdownMenuBox(
                        expanded = dishExpanded,
                        onExpandedChange = { dishExpanded = !dishExpanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = selectedDish?.let { "${it.emoji} ${it.name}" } ?: "Select Dish",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Dish") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dishExpanded) },
                            modifier = Modifier
                                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                                .fillMaxWidth()
                        )
                        
                        ExposedDropdownMenu(
                            expanded = dishExpanded,
                            onDismissRequest = { dishExpanded = false }
                        ) {
                            dishes.forEach { dish ->
                                DropdownMenuItem(
                                    text = { Text("${dish.emoji} ${dish.name}") },
                                    onClick = {
                                        selectedDishId = dish.id
                                        dishExpanded = false
                                    }
                                )
                            }
                        }
                    }
                } else {
                    var ingredientExpanded by remember { mutableStateOf(false) }
                    val selectedIngredient = ingredients.find { it.id == selectedIngredientId }
                    
                    ExposedDropdownMenuBox(
                        expanded = ingredientExpanded,
                        onExpandedChange = { ingredientExpanded = !ingredientExpanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = selectedIngredient?.let { 
                                if (it.emoji.isNotBlank()) "${it.emoji} ${it.name}" else it.name 
                            } ?: "Select Ingredient",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Ingredient") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = ingredientExpanded) },
                            modifier = Modifier
                                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                                .fillMaxWidth()
                        )
                        
                        ExposedDropdownMenu(
                            expanded = ingredientExpanded,
                            onDismissRequest = { ingredientExpanded = false }
                        ) {
                            ingredients.forEach { ingredient ->
                                DropdownMenuItem(
                                    text = { 
                                        Text(
                                            if (ingredient.emoji.isNotBlank()) "${ingredient.emoji} ${ingredient.name}" else ingredient.name
                                        ) 
                                    },
                                    onClick = {
                                        selectedIngredientId = ingredient.id
                                        ingredientExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
                
                OutlinedTextField(
                    value = quantity,
                    onValueChange = { quantity = it },
                    label = { Text("Quantity") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )
                
                OutlinedTextField(
                    value = dateFormat.format(selectedDate),
                    onValueChange = {},
                    label = { Text("Date & Time") },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth()
                )
                
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
                            val quantityValue = quantity.toDoubleOrNull() ?: 1.0
                            val dishId = if (selectedType == "dish") selectedDishId else null
                            val ingredientId = if (selectedType == "ingredient") selectedIngredientId else null
                            onSave(dishId, ingredientId, quantityValue, selectedDate)
                        },
                        enabled = quantity.toDoubleOrNull() != null && (
                            (selectedType == "dish" && selectedDishId != 0) ||
                            (selectedType == "ingredient" && selectedIngredientId != 0)
                        )
                    ) {
                        Text("Save")
                    }
                }
            }
        }
    }
}