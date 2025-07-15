package com.example.nutry.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nutry.utils.FreshnessColor

@Composable
fun FreshnessProgressBar(
    freshnessScore: Int,
    modifier: Modifier = Modifier,
    showText: Boolean = true
) {
    val progress = freshnessScore / 100f
    val color = getFreshnessColor(freshnessScore)
    
    Column(
        modifier = modifier
    ) {
        if (showText) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Freshness",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "${freshnessScore}%",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
        }
        
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
            color = color,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
            strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
        )
    }
}

@Composable
private fun getFreshnessColor(freshnessScore: Int): Color {
    return when {
        freshnessScore >= 80 -> Color(0xFF4CAF50) // Green
        freshnessScore >= 50 -> Color(0xFFFF9800) // Orange
        freshnessScore >= 20 -> Color(0xFFFF5722) // Deep Orange
        else -> Color(0xFFF44336) // Red
    }
}