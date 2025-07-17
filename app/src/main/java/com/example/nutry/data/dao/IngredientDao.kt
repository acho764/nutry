package com.example.nutry.data.dao

import androidx.room.*
import com.example.nutry.data.entities.Ingredient
import kotlinx.coroutines.flow.Flow

@Dao
interface IngredientDao {
    @Query("SELECT * FROM ingredients ORDER BY name ASC")
    fun getAllIngredients(): Flow<List<Ingredient>>
    
    @Query("SELECT * FROM ingredients ORDER BY name ASC")
    suspend fun getAllIngredientsSync(): List<Ingredient>

    @Query("SELECT * FROM ingredients WHERE categoryId = :categoryId ORDER BY name ASC")
    fun getIngredientsByCategory(categoryId: Int): Flow<List<Ingredient>>

    @Query("SELECT * FROM ingredients WHERE id = :id")
    suspend fun getIngredientById(id: Int): Ingredient?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIngredient(ingredient: Ingredient): Long

    @Update
    suspend fun updateIngredient(ingredient: Ingredient)

    @Delete
    suspend fun deleteIngredient(ingredient: Ingredient)

    @Query("DELETE FROM ingredients WHERE id = :id")
    suspend fun deleteIngredientById(id: Int)
    
    @Query("""
        SELECT i.id, i.name, i.categoryId, i.emoji, c.emoji as categoryEmoji 
        FROM ingredients i 
        INNER JOIN categories c ON i.categoryId = c.id 
        ORDER BY i.name ASC
    """)
    fun getAllIngredientsWithCategory(): Flow<List<com.example.nutry.data.entities.IngredientWithCategory>>
    
}