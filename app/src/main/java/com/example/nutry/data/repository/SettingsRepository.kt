package com.example.nutry.data.repository

import com.example.nutry.data.dao.SettingsDao
import com.example.nutry.data.entities.Settings
import kotlinx.coroutines.flow.Flow

class SettingsRepository(private val settingsDao: SettingsDao) {
    
    fun getSettings(): Flow<Settings?> = settingsDao.getSettings()
    
    suspend fun getSettingsSync(): Settings? = settingsDao.getSettingsSync()
    
    suspend fun insertSettings(settings: Settings) = settingsDao.insertSettings(settings)
    
    suspend fun updateSettings(settings: Settings) = settingsDao.updateSettings(settings)
    
    suspend fun getOrCreateSettings(): Settings {
        return getSettingsSync() ?: run {
            val defaultSettings = Settings()
            insertSettings(defaultSettings)
            defaultSettings
        }
    }
}