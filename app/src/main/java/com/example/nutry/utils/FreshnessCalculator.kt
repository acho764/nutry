package com.example.nutry.utils

import com.example.nutry.data.entities.Dish
import com.example.nutry.data.entities.Ingredient
import com.example.nutry.data.entities.TrackEntry
import java.util.Date

object FreshnessCalculator {
    
    /**
     * Calculate freshness score for an ingredient based on its lastEaten field
     * @param ingredient The ingredient to calculate freshness for
     * @param ingredientBasedTimewindow Number of days for 100% freshness
     * @return Freshness score as percentage (0-100)
     */
    fun calculateIngredientFreshness(
        ingredient: Ingredient,
        ingredientBasedTimewindow: Int
    ): Int {
        val lastEaten = ingredient.lastEaten
        
        if (lastEaten == null) {
            // Never consumed = 100% fresh
            return 100
        }
        
        val now = Date()
        return calculateFreshnessFromLastConsumption(lastEaten, now, ingredientBasedTimewindow)
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
     * @param dishIngredients List of ingredients in this dish
     * @param ingredientBasedTimewindow Number of days for 100% freshness for ingredients
     * @return Freshness score as percentage (0-100)
     */
    fun calculateDishFreshnessFromIngredients(
        dish: Dish,
        dishIngredients: List<Ingredient>,
        ingredientBasedTimewindow: Int
    ): Int {
        if (dishIngredients.isEmpty()) {
            return 100
        }
        
        // Calculate freshness for each ingredient using simplified method
        val ingredientFreshnesses = dishIngredients.map { ingredient ->
            calculateIngredientFreshness(ingredient, ingredientBasedTimewindow)
        }
        
        // Return average freshness of all ingredients
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