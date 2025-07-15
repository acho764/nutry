package com.example.nutry.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.nutry.data.entities.TrackEntry
import com.example.nutry.data.repository.TrackRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date

class TrackViewModel(private val repository: TrackRepository) : ViewModel() {
    
    private val _trackEntries = MutableStateFlow<List<TrackEntry>>(emptyList())
    val trackEntries: StateFlow<List<TrackEntry>> = _trackEntries.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    init {
        observeTrackEntries()
    }
    
    private fun observeTrackEntries() {
        viewModelScope.launch {
            repository.getAllTrackEntries().collect { entries ->
                _trackEntries.value = entries
            }
        }
    }
    
    fun insertTrackEntry(dishId: Int?, ingredientId: Int?, quantity: Double = 1.0) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                val trackEntry = TrackEntry(
                    dishId = dishId,
                    ingredientId = ingredientId,
                    consumedAt = Date(),
                    quantity = quantity
                )
                repository.insertTrackEntry(trackEntry)
            } catch (e: Exception) {
                _error.value = "Error adding track entry: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun updateTrackEntry(trackEntry: TrackEntry) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                repository.updateTrackEntry(trackEntry)
            } catch (e: Exception) {
                _error.value = "Error updating track entry: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun deleteTrackEntry(trackEntry: TrackEntry) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                repository.deleteTrackEntry(trackEntry)
            } catch (e: Exception) {
                _error.value = "Error deleting track entry: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun clearError() {
        _error.value = null
    }
}

class TrackViewModelFactory(
    private val repository: TrackRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TrackViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TrackViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}