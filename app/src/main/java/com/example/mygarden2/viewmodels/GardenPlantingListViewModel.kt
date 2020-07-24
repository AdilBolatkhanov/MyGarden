
package com.example.mygarden2.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import com.example.mygarden2.data.GardenPlantingRepository
import com.example.mygarden2.data.PlantAndGardenPlantings
import kotlinx.coroutines.launch


class GardenPlantingListViewModel internal constructor(
    val gardenPlantingRepository: GardenPlantingRepository
) : ViewModel() {
    val plantAndGardenPlantings: LiveData<List<PlantAndGardenPlantings>> =
            gardenPlantingRepository.getPlantedGardens()

    fun clearGarden(){
        viewModelScope.launch {
            gardenPlantingRepository.clearGarden()
            WorkManager.getInstance().cancelAllWork()
        }
    }
}