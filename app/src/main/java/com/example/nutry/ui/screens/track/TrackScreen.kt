package com.example.nutry.ui.screens.track

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nutry.NutryApplication
import com.example.nutry.data.entities.TrackEntry
import com.example.nutry.ui.components.TrackDialog
import com.example.nutry.ui.components.TrackItem
import com.example.nutry.ui.viewmodels.TrackViewModel
import com.example.nutry.ui.viewmodels.TrackViewModelFactory
import com.example.nutry.ui.viewmodels.DishViewModel
import com.example.nutry.ui.viewmodels.DishViewModelFactory
import com.example.nutry.ui.viewmodels.IngredientViewModel
import com.example.nutry.ui.viewmodels.IngredientViewModelFactory
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackScreen() {
    val context = LocalContext.current
    val application = context.applicationContext as NutryApplication
    val trackViewModel: TrackViewModel = viewModel(
        factory = TrackViewModelFactory(application.trackRepository)
    )
    val dishViewModel: DishViewModel = viewModel(
        factory = DishViewModelFactory(application.dishRepository)
    )
    val ingredientViewModel: IngredientViewModel = viewModel(
        factory = IngredientViewModelFactory(application.ingredientRepository)
    )
    
    val trackEntries by trackViewModel.trackEntries.collectAsState()
    val dishes by dishViewModel.dishes.collectAsState()
    val ingredients by ingredientViewModel.ingredients.collectAsState()
    val isLoading by trackViewModel.isLoading.collectAsState()
    val error by trackViewModel.error.collectAsState()
    
    var showAddDialog by remember { mutableStateOf(false) }
    var editingTrackEntry by remember { mutableStateOf<TrackEntry?>(null) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "📊 Track",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            
            FloatingActionButton(
                onClick = { showAddDialog = true },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Track Entry"
                )
            }
        }
        
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        
        error?.let { errorMessage ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
        
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(trackEntries) { trackEntry ->
                val dish = trackEntry.dishId?.let { id -> dishes.find { it.id == id } }
                val ingredient = trackEntry.ingredientId?.let { id -> ingredients.find { it.id == id } }
                
                TrackItem(
                    trackEntry = trackEntry,
                    dish = dish,
                    ingredient = ingredient,
                    onEdit = { editingTrackEntry = it },
                    onDelete = { trackViewModel.deleteTrackEntry(it) }
                )
            }
        }
    }
    
    if (showAddDialog) {
        TrackDialog(
            dishes = dishes,
            ingredients = ingredients,
            onDismiss = { showAddDialog = false },
            onSave = { dishId, ingredientId, quantity, date ->
                trackViewModel.insertTrackEntry(dishId, ingredientId, quantity)
                showAddDialog = false
            }
        )
    }
    
    editingTrackEntry?.let { trackEntry ->
        TrackDialog(
            trackEntry = trackEntry,
            dishes = dishes,
            ingredients = ingredients,
            onDismiss = { editingTrackEntry = null },
            onSave = { dishId, ingredientId, quantity, date ->
                trackViewModel.updateTrackEntry(
                    trackEntry.copy(
                        dishId = dishId,
                        ingredientId = ingredientId,
                        quantity = quantity,
                        consumedAt = date
                    )
                )
                editingTrackEntry = null
            }
        )
    }
}