package com.example.nutry.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "ingredients",
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Ingredient(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val categoryId: Int,
    val emoji: String = ""
)

data class IngredientWithCategoryData(
    val id: Int,
    val name: String,
    val categoryId: Int,
    val emoji: String,
    val categoryName: String,
    val categoryEmoji: String
)