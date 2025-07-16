package com.example.nutry.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.nutry.data.entities.Dish
import com.example.nutry.data.entities.Ingredient
import com.example.nutry.data.entities.Settings
import com.example.nutry.data.entities.TrackEntry
import com.example.nutry.data.repository.DishRepository
import com.example.nutry.data.repository.IngredientRepository
import com.example.nutry.data.repository.SettingsRepository
import com.example.nutry.data.repository.TrackRepository
import com.example.nutry.utils.FreshnessCalculator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

data class RecommendationItem(
    val dish: Dish? = null,
    val ingredient: Ingredient? = null,
    val freshnessScore: Int,
    val type: RecommendationType
)

enum class RecommendationType {
    DISH_BASED,
    INGREDIENT_BASED
}

class RecommendationsViewModel(
    private val dishRepository: DishRepository,
    private val ingredientRepository: IngredientRepository,
    private val trackRepository: TrackRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    
    private val _recommendations = MutableStateFlow<List<RecommendationItem>>(emptyList())
    val recommendations: StateFlow<List<RecommendationItem>> = _recommendations.asStateFlow()
    
    private val _recommendationMode = MutableStateFlow(RecommendationType.DISH_BASED)
    val recommendationMode: StateFlow<RecommendationType> = _recommendationMode.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    init {
        observeDataAndCalculateRecommendations()
    }
    
    private fun observeDataAndCalculateRecommendations() {
        viewModelScope.launch {
            combine(
                dishRepository.getAllDishes(),
                ingredientRepository.getAllIngredients(),
                trackRepository.getAllTrackEntries(),
                settingsRepository.getSettings(),
                _recommendationMode
            ) { dishes, ingredients, trackEntries, settings, mode ->
                calculateRecommendations(dishes, ingredients, trackEntries, settings ?: Settings())
            }.collect { recommendations ->
                _recommendations.value = recommendations
            }
        }
    }
    
    private suspend fun calculateRecommendations(
        dishes: List<Dish>,
        ingredients: List<Ingredient>,
        trackEntries: List<TrackEntry>,
        settings: Settings
    ): List<RecommendationItem> {
        return try {
            _isLoading.value = true
            _error.value = null
            
            when (_recommendationMode.value) {
                RecommendationType.DISH_BASED -> {
                    calculateDishBasedRecommendations(dishes, trackEntries, settings)
                }
                RecommendationType.INGREDIENT_BASED -> {
                    calculateIngredientBasedRecommendations(dishes, ingredients, trackEntries, settings)
                }
            }
        } catch (e: Exception) {
            _error.value = "Error calculating recommendations: ${e.message}"
            emptyList()
        } finally {
            _isLoading.value = false
        }
    }
    
    private suspend fun calculateDishBasedRecommendations(
        dishes: List<Dish>,
        trackEntries: List<TrackEntry>,
        settings: Settings
    ): List<RecommendationItem> {
        return dishes.map { dish ->
            val freshnessScore = FreshnessCalculator.calculateDishFreshness(
                dish, trackEntries, settings.dishBasedTimewindow
            )
            RecommendationItem(
                dish = dish,
                freshnessScore = freshnessScore,
                type = RecommendationType.DISH_BASED
            )
        }.sortedByDescending { it.freshnessScore }
    }
    
    private suspend fun calculateIngredientBasedRecommendations(
        dishes: List<Dish>,
        ingredients: List<Ingredient>,
        trackEntries: List<TrackEntry>,
        settings: Settings
    ): List<RecommendationItem> {
        return dishes.map { dish ->
            val dishIngredients = dishRepository.getIngredientsByDish(dish.id)
            val freshnessScore = FreshnessCalculator.calculateDishFreshnessFromIngredients(
                dish, dishIngredients, settings.ingredientBasedTimewindow
            )
            RecommendationItem(
                dish = dish,
                freshnessScore = freshnessScore,
                type = RecommendationType.INGREDIENT_BASED
            )
        }.sortedByDescending { it.freshnessScore }
    }
    
    fun setRecommendationMode(mode: RecommendationType) {
        _recommendationMode.value = mode
        // Recalculation happens automatically via the combine flow
    }
    
    fun clearError() {
        _error.value = null
    }
}

class RecommendationsViewModelFactory(
    private val dishRepository: DishRepository,
    private val ingredientRepository: IngredientRepository,
    private val trackRepository: TrackRepository,
    private val settingsRepository: SettingsRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecommendationsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RecommendationsViewModel(
                dishRepository, ingredientRepository, trackRepository, settingsRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}