package com.example.nutry.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.nutry.data.entities.Category
import com.example.nutry.data.repository.CategoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CategoryViewModel(private val repository: CategoryRepository) : ViewModel() {
    
    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    init {
        observeCategories()
    }
    
    private fun observeCategories() {
        viewModelScope.launch {
            repository.getAllCategories().collect { categoryList ->
                _categories.value = categoryList
            }
        }
    }
    
    fun insertCategory(name: String, emoji: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                val category = Category(name = name, emoji = emoji)
                repository.insertCategory(category)
            } catch (e: Exception) {
                _error.value = "Error adding category: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun updateCategory(category: Category) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                repository.updateCategory(category)
            } catch (e: Exception) {
                _error.value = "Error updating category: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun deleteCategory(category: Category) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                repository.deleteCategory(category)
            } catch (e: Exception) {
                _error.value = "Error deleting category: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun clearError() {
        _error.value = null
    }
}

class CategoryViewModelFactory(
    private val repository: CategoryRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CategoryViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}