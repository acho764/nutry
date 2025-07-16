package com.example.nutry.data.repository

import com.example.nutry.data.dao.TrackDao
import com.example.nutry.data.dao.IngredientDao
import com.example.nutry.data.dao.DishDao
import com.example.nutry.data.entities.TrackEntry
import kotlinx.coroutines.flow.Flow
import java.util.Date

class TrackRepository(
    private val trackDao: TrackDao,
    private val ingredientDao: IngredientDao,
    private val dishDao: DishDao
) {
    
    fun getAllTrackEntries(): Flow<List<TrackEntry>> = trackDao.getAllTrackEntries()
    
    suspend fun getTrackEntryById(id: Int): TrackEntry? = trackDao.getTrackEntryById(id)
    
    suspend fun getTrackEntriesByDish(dishId: Int): List<TrackEntry> = 
        trackDao.getTrackEntriesByDish(dishId)
    
    suspend fun getTrackEntriesByIngredient(ingredientId: Int): List<TrackEntry> = 
        trackDao.getTrackEntriesByIngredient(ingredientId)
    
    suspend fun getLastDishConsumption(dishId: Int, fromDate: Date): TrackEntry? = 
        trackDao.getLastDishConsumption(dishId, fromDate)
    
    suspend fun getLastIngredientConsumption(ingredientId: Int, fromDate: Date): TrackEntry? = 
        trackDao.getLastIngredientConsumption(ingredientId, fromDate)
    
    suspend fun insertTrackEntry(trackEntry: TrackEntry): Long {
        val id = trackDao.insertTrackEntry(trackEntry)
        
        // Update lastEaten for ingredients
        if (trackEntry.ingredientId != null) {
            // Direct ingredient consumption
            ingredientDao.updateIngredientLastEaten(trackEntry.ingredientId!!, trackEntry.consumedAt)
        } else if (trackEntry.dishId != null) {
            // Dish consumption - update lastEaten for all ingredients in the dish
            val ingredientIds = dishDao.getIngredientIdsByDish(trackEntry.dishId!!)
            ingredientIds.forEach { ingredientId ->
                ingredientDao.updateIngredientLastEaten(ingredientId, trackEntry.consumedAt)
            }
        }
        
        return id
    }
    
    suspend fun updateTrackEntry(trackEntry: TrackEntry) = trackDao.updateTrackEntry(trackEntry)
    
    suspend fun deleteTrackEntry(trackEntry: TrackEntry) = trackDao.deleteTrackEntry(trackEntry)
    
    suspend fun deleteTrackEntryById(id: Int) = trackDao.deleteTrackEntryById(id)
}