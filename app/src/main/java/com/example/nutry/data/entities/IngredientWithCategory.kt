package com.example.nutry.data.entities

data class IngredientWithCategory(
    val id: Int,
    val name: String,
    val categoryId: Int,
    val emoji: String,
    val categoryEmoji: String
)