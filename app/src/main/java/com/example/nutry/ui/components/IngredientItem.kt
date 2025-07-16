package com.example.nutry.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nutry.data.entities.Category
import com.example.nutry.data.entities.Ingredient
import com.example.nutry.ui.components.FreshnessProgressBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IngredientItem(
    ingredient: Ingredient,
    category: Category?,
    freshnessScore: Int,
    onEdit: (Ingredient) -> Unit,
    onDelete: (Ingredient) -> Unit,
    onEat: (Ingredient) -> Unit,
    onClick: (Ingredient) -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick(ingredient) }
                .padding(12.dp)
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
                        text = if (ingredient.emoji.isNotBlank()) "${ingredient.emoji} ${ingredient.name}" else ingredient.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                    category?.let {
                        Text(
                            text = "${it.emoji} ${it.name}",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
                
                Row {
                    IconButton(onClick = { onEat(ingredient) }) {
                        Icon(
                            imageVector = Icons.Default.Restaurant,
                            contentDescription = "Eat",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            FreshnessProgressBar(
                freshnessScore = freshnessScore,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
    
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Ingredient") },
            text = { Text("Are you sure you want to delete \"${ingredient.name}\"? This will also remove it from all dishes and tracking records.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDelete(ingredient)
                        showDeleteDialog = false
                    }
                ) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}