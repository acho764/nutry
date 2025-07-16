package com.example.nutry.utils

import com.example.nutry.data.entities.Dish
import com.example.nutry.data.entities.Ingredient
import com.example.nutry.data.entities.TrackEntry
import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeUnit

object FreshnessCalculator {
    
    /**
     * Calculate freshness score for an ingredient based on its consumption history
     * @param ingredient The ingredient to calculate freshness for
     * @param trackEntries List of track entries for this ingredient
     * @param ingredientBasedTimewindow Number of days for 100% freshness
     * @return Freshness score as percentage (0-100)
     */
    fun calculateIngredientFreshness(
        ingredient: Ingredient,
        trackEntries: List<TrackEntry>,
        ingredientBasedTimewindow: Int,
        dishIngredientRelationships: List<com.example.nutry.data.entities.DishIngredient> = emptyList()
    ): Int {
        val now = Date()
        val timewindowInMillis = ingredientBasedTimewindow * 24 * 60 * 60 * 1000L
        val cutoffDate = Date(now.time - timewindowInMillis)
        
        // Filter track entries for this ingredient within the timewindow
        val directIngredientEntries = trackEntries.filter { entry ->
            entry.ingredientId == ingredient.id && entry.consumedAt.after(cutoffDate)
        }
        
        // Also consider dish consumption that contains this ingredient
        val dishesContainingIngredient = dishIngredientRelationships.filter { 
            it.ingredientId == ingredient.id 
        }.map { it.dishId }
        
        val dishConsumptionEntries = trackEntries.filter { entry ->
            entry.dishId != null && 
            dishesContainingIngredient.contains(entry.dishId) && 
            entry.consumedAt.after(cutoffDate)
        }
        
        val recentEntries = directIngredientEntries + dishConsumptionEntries
        
        if (recentEntries.isEmpty()) {
            // No consumption in the timewindow = 100% fresh
            return 100
        }
        
        // Find the most recent consumption
        val mostRecentEntry = recentEntries.maxByOrNull { it.consumedAt }
        
        return if (mostRecentEntry != null) {
            calculateFreshnessFromLastConsumption(mostRecentEntry.consumedAt, now, ingredientBasedTimewindow)
        } else {
            100
        }
    }
    
    /**
     * Calculate freshness score for a dish based on dish consumption history
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
        val now = Date()
        val timewindowInMillis = dishBasedTimewindow * 24 * 60 * 60 * 1000L
        val cutoffDate = Date(now.time - timewindowInMillis)
        
        // Filter track entries for this dish within the timewindow
        val recentEntries = trackEntries.filter { entry ->
            entry.dishId == dish.id && entry.consumedAt.after(cutoffDate)
        }
        
        if (recentEntries.isEmpty()) {
            // No consumption in the timewindow = 100% fresh
            return 100
        }
        
        // Find the most recent consumption
        val mostRecentEntry = recentEntries.maxByOrNull { it.consumedAt }
        
        return if (mostRecentEntry != null) {
            calculateFreshnessFromLastConsumption(mostRecentEntry.consumedAt, now, dishBasedTimewindow)
        } else {
            100
        }
    }
    
    /**
     * Calculate freshness score for a dish based on its ingredients' freshness
     * @param dish The dish to calculate freshness for
     * @param dishIngredients List of ingredients in this dish
     * @param allTrackEntries All track entries for ingredient lookups
     * @param ingredientBasedTimewindow Number of days for 100% freshness for ingredients
     * @return Freshness score as percentage (0-100)
     */
    fun calculateDishFreshnessFromIngredients(
        dish: Dish,
        dishIngredients: List<Ingredient>,
        allTrackEntries: List<TrackEntry>,
        ingredientBasedTimewindow: Int
    ): Int {
        if (dishIngredients.isEmpty()) {
            return 100
        }
        
        // Calculate freshness for each ingredient
        val ingredientFreshnesses = dishIngredients.map { ingredient ->
            calculateIngredientFreshness(ingredient, allTrackEntries, ingredientBasedTimewindow, emptyList())
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