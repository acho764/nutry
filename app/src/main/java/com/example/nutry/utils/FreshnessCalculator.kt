package com.example.nutry.utils

import com.example.nutry.data.entities.Dish
import com.example.nutry.data.entities.Ingredient
import com.example.nutry.data.entities.TrackEntry
import com.example.nutry.data.entities.Category
import java.util.Date

object FreshnessCalculator {
    
    /**
     * Calculate freshness score for an ingredient based on track entries
     * @param ingredient The ingredient to calculate freshness for
     * @param trackEntries List of track entries for this ingredient
     * @param ingredientBasedTimewindow Number of days for 100% freshness
     * @param category The category of the ingredient
     * @param excludeSpices Whether to exclude spices from freshness calculation (returns 100% for individual spice display)
     * @return Freshness score as percentage (0-100)
     */
    fun calculateIngredientFreshness(
        ingredient: Ingredient,
        trackEntries: List<TrackEntry>,
        ingredientBasedTimewindow: Int,
        category: Category? = null,
        excludeSpices: Boolean = false
    ): Int {
        // If excluding spices and this is a spice, return 100% (always fresh)
        if (excludeSpices && (category?.name?.contains("Подправки") == true || category?.name?.contains("Spices") == true)) {
            return 100
        }
        
        // Find the most recent consumption of this ingredient (direct consumption only)
        val mostRecentEntry = trackEntries
            .filter { it.ingredientId == ingredient.id }
            .maxByOrNull { it.consumedAt }
        
        if (mostRecentEntry == null) {
            // Never consumed = 100% fresh
            return 100
        }
        
        val now = Date()
        return calculateFreshnessFromLastConsumption(mostRecentEntry.consumedAt, now, ingredientBasedTimewindow)
    }
    
    /**
     * Calculate freshness score for a dish based on track consumption
     * @param dish The dish to calculate freshness for
     * @param trackEntries List of track entries for this dish
     * @param dishBasedTimewindow Number of days for 100% freshness
     * @return Freshness score as percentage (0-100)
     */
    fun calculateDishFreshness(
        dish: Dish,
        trackEntries: List<TrackEntry>,
        dishBasedTimewindow: Int
    ): Int {
        // Find the most recent consumption of this dish
        val mostRecentEntry = trackEntries
            .filter { it.dishId == dish.id }
            .maxByOrNull { it.consumedAt }
        
        if (mostRecentEntry == null) {
            // Never consumed = 100% fresh
            return 100
        }
        
        val now = Date()
        return calculateFreshnessFromLastConsumption(mostRecentEntry.consumedAt, now, dishBasedTimewindow)
    }
    
    /**
     * Calculate freshness score for a dish based on its ingredients' freshness
     * @param dish The dish to calculate freshness for
     * @param dishIngredients List of ingredients in this dish with their categories
     * @param trackEntries List of track entries for freshness calculation
     * @param ingredientBasedTimewindow Number of days for 100% freshness for ingredients
     * @param excludeSpices Whether to exclude spices from freshness calculation
     * @return Freshness score as percentage (0-100)
     */
    fun calculateDishFreshnessFromIngredients(
        dish: Dish,
        dishIngredients: List<Pair<Ingredient, Category?>>,
        trackEntries: List<TrackEntry>,
        ingredientBasedTimewindow: Int,
        excludeSpices: Boolean = false
    ): Int {
        if (dishIngredients.isEmpty()) {
            return 100
        }
        
        // Filter out spices if excludeSpices is enabled
        val relevantIngredients = if (excludeSpices) {
            dishIngredients.filter { (ingredient, category) ->
                !(category?.name?.contains("Подправки") == true || category?.name?.contains("Spices") == true)
            }
        } else {
            dishIngredients
        }
        
        // If no relevant ingredients remain after filtering, return 100%
        if (relevantIngredients.isEmpty()) {
            return 100
        }
        
        // Calculate freshness for each relevant ingredient (without excludeSpices override)
        val ingredientFreshnesses = relevantIngredients.map { (ingredient, category) ->
            calculateIngredientFreshness(ingredient, trackEntries, ingredientBasedTimewindow, category, excludeSpices = false)
        }
        
        // Return average freshness of relevant ingredients only
        return ingredientFreshnesses.average().toInt()
    }
    
    /**
     * Calculate freshness score based on time since last consumption
     * @param lastConsumption Date of last consumption
     * @param now Current date
     * @param timewindow Number of days for 100% freshness
     * @return Freshness score as percentage (0-100)
     */
    private fun calculateFreshnessFromLastConsumption(
        lastConsumption: Date,
        now: Date,
        timewindow: Int
    ): Int {
        val millisSinceConsumption = now.time - lastConsumption.time
        val timewindowInMillis = timewindow * 24 * 60 * 60 * 1000L // Convert days to milliseconds
        
        return when {
            millisSinceConsumption >= timewindowInMillis -> 100 // Full freshness after timewindow
            millisSinceConsumption <= 0 -> 0 // Just consumed = 0% fresh
            else -> {
                // Linear interpolation between 0% and 100% using precise timestamps
                val freshnessPercentage = (millisSinceConsumption.toDouble() / timewindowInMillis) * 100
                freshnessPercentage.toInt().coerceIn(0, 100)
            }
        }
    }
    
    /**
     * Get freshness color based on score
     * @param freshnessScore Freshness score (0-100)
     * @return Color resource identifier for the freshness level
     */
    fun getFreshnessColor(freshnessScore: Int): FreshnessColor {
        return when {
            freshnessScore >= 80 -> FreshnessColor.HIGH
            freshnessScore >= 50 -> FreshnessColor.MEDIUM
            freshnessScore >= 20 -> FreshnessColor.LOW
            else -> FreshnessColor.CRITICAL
        }
    }
}

enum class FreshnessColor {
    HIGH,    // Green - 80-100%
    MEDIUM,  // Yellow - 50-79%
    LOW,     // Orange - 20-49%
    CRITICAL // Red - 0-19%
}