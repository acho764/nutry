package com.example.nutry.data.repository

import com.example.nutry.data.dao.DishDao
import com.example.nutry.data.entities.Dish
import com.example.nutry.data.entities.DishIngredient
import com.example.nutry.data.entities.Ingredient
import com.example.nutry.data.entities.IngredientWithCategory
import kotlinx.coroutines.flow.Flow

class DishRepository(private val dishDao: DishDao) {
    
    fun getAllDishes(): Flow<List<Dish>> = dishDao.getAllDishes()
    
    suspend fun getDishById(id: Int): Dish? = dishDao.getDishById(id)
    
    suspend fun insertDish(dish: Dish): Long = dishDao.insertDish(dish)
    
    suspend fun updateDish(dish: Dish) = dishDao.updateDish(dish)
    
    suspend fun deleteDish(dish: Dish) = dishDao.deleteDish(dish)
    
    suspend fun deleteDishById(id: Int) = dishDao.deleteDishById(id)
    
    suspend fun insertDishIngredient(dishIngredient: DishIngredient) = 
        dishDao.insertDishIngredient(dishIngredient)
    
    suspend fun deleteDishIngredients(dishId: Int) = dishDao.deleteDishIngredients(dishId)
    
    suspend fun deleteDishIngredient(dishId: Int, ingredientId: Int) = 
        dishDao.deleteDishIngredient(dishId, ingredientId)
    
    suspend fun getDishIngredients(dishId: Int): List<DishIngredient> = 
        dishDao.getDishIngredients(dishId)
    
    suspend fun getAllDishIngredients(): List<DishIngredient> = 
        dishDao.getAllDishIngredients()
    
    suspend fun getIngredientsByDish(dishId: Int): List<Ingredient> = 
        dishDao.getIngredientsByDish(dishId)
    
    suspend fun getIngredientsWithCategoryByDish(dishId: Int): List<IngredientWithCategory> = 
        dishDao.getIngredientsWithCategoryByDish(dishId)
}