package com.example.nutry.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.nutry.data.entities.Settings
import com.example.nutry.data.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(private val repository: SettingsRepository) : ViewModel() {
    
    private val _settings = MutableStateFlow(Settings())
    val settings: StateFlow<Settings> = _settings.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    init {
        loadSettings()
    }
    
    private fun loadSettings() {
        viewModelScope.launch {
            repository.getSettings().collect { settings ->
                _settings.value = settings ?: Settings()
            }
        }
    }
    
    fun updateSettings(newSettings: Settings) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                repository.updateSettings(newSettings)
            } catch (e: Exception) {
                _error.value = "Error updating settings: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun updateIngredientTimewindow(days: Int) {
        val currentSettings = _settings.value
        updateSettings(currentSettings.copy(ingredientBasedTimewindow = days))
    }
    
    fun updateDishTimewindow(days: Int) {
        val currentSettings = _settings.value
        updateSettings(currentSettings.copy(dishBasedTimewindow = days))
    }
    
    fun clearError() {
        _error.value = null
    }
}

class SettingsViewModelFactory(
    private val repository: SettingsRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SettingsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}