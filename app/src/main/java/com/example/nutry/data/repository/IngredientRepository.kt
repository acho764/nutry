package com.example.nutry.data.repository

import com.example.nutry.data.dao.IngredientDao
import com.example.nutry.data.entities.Ingredient
import com.example.nutry.data.entities.IngredientWithCategory
import kotlinx.coroutines.flow.Flow

class IngredientRepository(private val ingredientDao: IngredientDao) {
    
    fun getAllIngredients(): Flow<List<Ingredient>> = ingredientDao.getAllIngredients()
    
    fun getIngredientsByCategory(categoryId: Int): Flow<List<Ingredient>> = 
        ingredientDao.getIngredientsByCategory(categoryId)
    
    suspend fun getIngredientById(id: Int): Ingredient? = ingredientDao.getIngredientById(id)
    
    suspend fun insertIngredient(ingredient: Ingredient): Long = ingredientDao.insertIngredient(ingredient)
    
    suspend fun updateIngredient(ingredient: Ingredient) = ingredientDao.updateIngredient(ingredient)
    
    suspend fun deleteIngredient(ingredient: Ingredient) = ingredientDao.deleteIngredient(ingredient)
    
    suspend fun deleteIngredientById(id: Int) = ingredientDao.deleteIngredientById(id)
    
    fun getAllIngredientsWithCategory(): Flow<List<IngredientWithCategory>> = 
        ingredientDao.getAllIngredientsWithCategory()
}