package com.example.nutry.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nutry.data.entities.Dish
import com.example.nutry.data.entities.Ingredient
import com.example.nutry.data.entities.TrackEntry
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackItem(
    trackEntry: TrackEntry,
    dish: Dish? = null,
    ingredient: Ingredient? = null,
    onEdit: (TrackEntry) -> Unit,
    onDelete: (TrackEntry) -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    when {
                        dish != null -> {
                            Text(
                                text = dish.emoji,
                                fontSize = 20.sp,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text(
                                text = dish.name,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        ingredient != null -> {
                            if (ingredient.emoji.isNotBlank()) {
                                Text(
                                    text = ingredient.emoji,
                                    fontSize = 20.sp,
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                            }
                            Text(
                                text = ingredient.name,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        else -> {
                            Text(
                                text = "Unknown item",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
                
                Text(
                    text = "Quantity: ${trackEntry.quantity}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
                
                Text(
                    text = dateFormat.format(trackEntry.consumedAt),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
            
            Row {
                IconButton(onClick = { onEdit(trackEntry) }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
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
    }
    
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Track Entry") },
            text = { Text("Are you sure you want to delete this consumption record?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDelete(trackEntry)
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