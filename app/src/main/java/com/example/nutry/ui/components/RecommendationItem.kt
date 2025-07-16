package com.example.nutry.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nutry.ui.viewmodels.RecommendationItem
import com.example.nutry.ui.viewmodels.RecommendationType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecommendationItemCard(
    recommendation: RecommendationItem,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    when (recommendation.type) {
                        RecommendationType.DISH_BASED,
                        RecommendationType.INGREDIENT_BASED -> {
                            recommendation.dish?.let { dish ->
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
                        }
                    }
                }
                
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = getRecommendationTypeText(recommendation.type),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            FreshnessProgressBar(
                freshnessScore = recommendation.freshnessScore,
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = getFreshnessMessage(recommendation.freshnessScore),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

private fun getRecommendationTypeText(type: RecommendationType): String {
    return when (type) {
        RecommendationType.DISH_BASED -> "Dish-based"
        RecommendationType.INGREDIENT_BASED -> "Ingredient-based"
    }
}

private fun getFreshnessMessage(freshnessScore: Int): String {
    return when {
        freshnessScore >= 80 -> "🌟 Perfect time to enjoy this!"
        freshnessScore >= 60 -> "😊 Good choice for today"
        freshnessScore >= 40 -> "🤔 Consider trying this soon"
        freshnessScore >= 20 -> "⏰ You had this recently"
        else -> "🔄 Try something else - you just had this"
    }
}