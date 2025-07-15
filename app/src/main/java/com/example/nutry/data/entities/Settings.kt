package com.example.nutry.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "settings")
data class Settings(
    @PrimaryKey
    val id: Int = 1,
    val ingredientBasedTimewindow: Int = 7,
    val dishBasedTimewindow: Int = 14
)