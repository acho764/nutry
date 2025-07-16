package com.example.nutry.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.nutry.data.entities.Dish
import com.example.nutry.data.entities.DishIngredient
import com.example.nutry.data.entities.Ingredient
import com.example.nutry.data.entities.IngredientWithCategory
import com.example.nutry.data.repository.DishRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DishViewModel(private val repository: DishRepository) : ViewModel() {
    
    private val _dishes = MutableStateFlow<List<Dish>>(emptyList())
    val dishes: StateFlow<List<Dish>> = _dishes.asStateFlow()
    
    private val _dishIngredients = MutableStateFlow<Map<Int, List<IngredientWithCategory>>>(emptyMap())
    val dishIngredients: StateFlow<Map<Int, List<IngredientWithCategory>>> = _dishIngredients.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    init {
        observeDishes()
    }
    
    private fun observeDishes() {
        viewModelScope.launch {
            repository.getAllDishes().collect { dishList ->
                _dishes.value = dishList
                loadIngredientsForDishes(dishList)
            }
        }
    }
    
    private fun loadIngredientsForDishes(dishes: List<Dish>) {
        viewModelScope.launch {
            val ingredientsMap = mutableMapOf<Int, List<IngredientWithCategory>>()
            dishes.forEach { dish ->
                try {
                    val ingredients = repository.getIngredientsWithCategoryByDish(dish.id)
                    ingredientsMap[dish.id] = ingredients
                } catch (e: Exception) {
                    // Handle individual dish ingredient loading errors
                }
            }
            _dishIngredients.value = ingredientsMap
        }
    }
    
    fun insertDish(name: String, emoji: String, ingredientIds: List<Int>) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                
                val dish = Dish(name = name, emoji = emoji)
                val dishId = repository.insertDish(dish)
                
                // Add ingredients to dish
                ingredientIds.forEach { ingredientId ->
                    val dishIngredient = DishIngredient(
                        dishId = dishId.toInt(),
                        ingredientId = ingredientId
                    )
                    repository.insertDishIngredient(dishIngredient)
                }
                
                // Refresh all dish ingredients to ensure UI is updated
                loadIngredientsForDishes(_dishes.value)
            } catch (e: Exception) {
                _error.value = "Error adding dish: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun updateDish(dish: Dish, ingredientIds: List<Int>) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                
                repository.updateDish(dish)
                
                // Update ingredients - remove all and add new ones
                repository.deleteDishIngredients(dish.id)
                ingredientIds.forEach { ingredientId ->
                    val dishIngredient = DishIngredient(
                        dishId = dish.id,
                        ingredientId = ingredientId
                    )
                    repository.insertDishIngredient(dishIngredient)
                }
                
                // Refresh all dish ingredients to ensure UI is updated
                loadIngredientsForDishes(_dishes.value)
            } catch (e: Exception) {
                _error.value = "Error updating dish: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun deleteDish(dish: Dish) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                repository.deleteDish(dish)
            } catch (e: Exception) {
                _error.value = "Error deleting dish: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun clearError() {
        _error.value = null
    }
}

class DishViewModelFactory(
    private val repository: DishRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DishViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DishViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}