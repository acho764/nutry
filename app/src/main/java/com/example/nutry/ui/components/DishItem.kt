package com.example.nutry.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nutry.data.entities.Dish
import com.example.nutry.data.entities.IngredientWithCategory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DishItem(
    dish: Dish,
    ingredients: List<IngredientWithCategory>,
    onEdit: (Dish) -> Unit,
    onDelete: (Dish) -> Unit
) {
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onEdit(dish) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = dish.emoji,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(end = 12.dp)
                )
                Text(
                    text = dish.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            
            if (ingredients.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Ingredients:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(ingredients) { ingredient ->
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer
                            ),
                            modifier = Modifier.padding(vertical = 2.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = if (ingredient.emoji.isNotBlank()) "${ingredient.emoji} ${ingredient.name}" else ingredient.name,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}