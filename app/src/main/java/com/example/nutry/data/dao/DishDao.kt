package com.example.nutry.data.dao

import androidx.room.*
import com.example.nutry.data.entities.Dish
import com.example.nutry.data.entities.DishIngredient
import com.example.nutry.data.entities.IngredientWithCategoryData
import kotlinx.coroutines.flow.Flow

@Dao
interface DishDao {
    @Query("SELECT * FROM dishes ORDER BY name ASC")
    fun getAllDishes(): Flow<List<Dish>>

    @Query("SELECT * FROM dishes WHERE id = :id")
    suspend fun getDishById(id: Int): Dish?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDish(dish: Dish): Long

    @Update
    suspend fun updateDish(dish: Dish)

    @Delete
    suspend fun deleteDish(dish: Dish)

    @Query("DELETE FROM dishes WHERE id = :id")
    suspend fun deleteDishById(id: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDishIngredient(dishIngredient: DishIngredient)

    @Query("DELETE FROM dish_ingredients WHERE dishId = :dishId")
    suspend fun deleteDishIngredients(dishId: Int)

    @Query("DELETE FROM dish_ingredients WHERE dishId = :dishId AND ingredientId = :ingredientId")
    suspend fun deleteDishIngredient(dishId: Int, ingredientId: Int)

    @Query("SELECT * FROM dish_ingredients WHERE dishId = :dishId")
    suspend fun getDishIngredients(dishId: Int): List<DishIngredient>

    @Query("SELECT * FROM dish_ingredients")
    suspend fun getAllDishIngredients(): List<DishIngredient>

    @Query("""
        SELECT i.* FROM ingredients i 
        INNER JOIN dish_ingredients di ON i.id = di.ingredientId 
        WHERE di.dishId = :dishId
    """)
    suspend fun getIngredientsByDish(dishId: Int): List<com.example.nutry.data.entities.Ingredient>
    
    @Query("""
        SELECT i.id, i.name, i.categoryId, i.emoji, c.emoji as categoryEmoji 
        FROM ingredients i 
        INNER JOIN categories c ON i.categoryId = c.id
        INNER JOIN dish_ingredients di ON i.id = di.ingredientId 
        WHERE di.dishId = :dishId
    """)
    suspend fun getIngredientsWithCategoryByDish(dishId: Int): List<com.example.nutry.data.entities.IngredientWithCategory>
    
    @Query("SELECT ingredientId FROM dish_ingredients WHERE dishId = :dishId")
    suspend fun getIngredientIdsByDish(dishId: Int): List<Int>
    
    @Query("""
        SELECT i.*, c.name as categoryName, c.emoji as categoryEmoji
        FROM ingredients i 
        INNER JOIN categories c ON i.categoryId = c.id
        INNER JOIN dish_ingredients di ON i.id = di.ingredientId 
        WHERE di.dishId = :dishId
    """)
    suspend fun getIngredientsWithCategoriesByDish(dishId: Int): List<IngredientWithCategoryData>
}