package com.example.nutry

import android.app.Application
import com.example.nutry.data.NutryDatabase
import com.example.nutry.data.repository.CategoryRepository
import com.example.nutry.data.repository.IngredientRepository
import com.example.nutry.data.repository.DishRepository
import com.example.nutry.data.repository.TrackRepository
import com.example.nutry.data.repository.SettingsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class NutryApplication : Application() {
    
    val applicationScope = CoroutineScope(SupervisorJob())
    
    val database by lazy { NutryDatabase.getDatabase(this, applicationScope) }
    val categoryRepository by lazy { CategoryRepository(database.categoryDao()) }
    val ingredientRepository by lazy { IngredientRepository(database.ingredientDao()) }
    val dishRepository by lazy { DishRepository(database.dishDao()) }
    val trackRepository by lazy { TrackRepository(database.trackDao()) }
    val settingsRepository by lazy { SettingsRepository(database.settingsDao()) }
}