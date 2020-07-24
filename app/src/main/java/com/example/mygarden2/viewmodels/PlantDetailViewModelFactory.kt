
package com.example.mygarden2.viewmodels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mygarden2.data.GardenPlantingRepository
import com.example.mygarden2.data.PlantRepository


/**
 * Factory for creating a [PlantDetailViewModel] with a constructor that takes a [PlantRepository]
 * and an ID for the current [Plant].
 */
class PlantDetailViewModelFactory(
    private val plantRepository: PlantRepository,
    private val gardenPlantingRepository: GardenPlantingRepository,
    private val plantId: String
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PlantDetailViewModel(plantRepository, gardenPlantingRepository, plantId) as T
    }
}
