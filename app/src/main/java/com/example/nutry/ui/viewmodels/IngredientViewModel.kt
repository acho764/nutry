package com.example.nutry.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.nutry.data.entities.Ingredient
import com.example.nutry.data.repository.IngredientRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class IngredientViewModel(private val repository: IngredientRepository) : ViewModel() {
    
    private val _ingredients = MutableStateFlow<List<Ingredient>>(emptyList())
    val ingredients: StateFlow<List<Ingredient>> = _ingredients.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    init {
        observeIngredients()
    }
    
    private fun observeIngredients() {
        viewModelScope.launch {
            repository.getAllIngredients().collect { ingredientList ->
                _ingredients.value = ingredientList
            }
        }
    }
    
    fun getIngredientsByCategory(categoryId: Int) {
        viewModelScope.launch {
            repository.getIngredientsByCategory(categoryId).collect { ingredientList ->
                _ingredients.value = ingredientList
            }
        }
    }
    
    fun insertIngredient(name: String, categoryId: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                val ingredient = Ingredient(name = name, categoryId = categoryId)
                repository.insertIngredient(ingredient)
            } catch (e: Exception) {
                _error.value = "Error adding ingredient: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun updateIngredient(ingredient: Ingredient) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                repository.updateIngredient(ingredient)
            } catch (e: Exception) {
                _error.value = "Error updating ingredient: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun deleteIngredient(ingredient: Ingredient) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                repository.deleteIngredient(ingredient)
            } catch (e: Exception) {
                _error.value = "Error deleting ingredient: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun clearError() {
        _error.value = null
    }
}

class IngredientViewModelFactory(
    private val repository: IngredientRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(IngredientViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return IngredientViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}