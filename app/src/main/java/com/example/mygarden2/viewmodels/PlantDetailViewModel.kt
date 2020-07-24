
package com.example.mygarden2.viewmodels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.*
import com.example.mygarden2.data.GardenPlantingRepository
import com.example.mygarden2.data.Plant
import com.example.mygarden2.data.PlantRepository
import com.example.mygarden2.workers.NeedWaterWorker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

/**
 * The ViewModel used in [PlantDetailFragment].
 */
const val NAME_OF_PLANT = "NAME_OF_PLANT"
class PlantDetailViewModel(
    val plantRepository: PlantRepository,
    private val gardenPlantingRepository: GardenPlantingRepository,
    private val plantId: String
) : ViewModel() {

    val isPlanted = gardenPlantingRepository.isPlanted(plantId)
    val plant = plantRepository.getPlant(plantId)

//    fun addPlantToGarden() {
//        viewModelScope.launch {
//            gardenPlantingRepository.createGardenPlanting(plantId)
//        }
//    }

    var isAddDelete: Boolean = false
    fun doAddDeletePlantToGarden() {
        viewModelScope.launch(Dispatchers.IO) {
            val myPlant = plantRepository.getSpecificPlant(plantId)
            val constraints = Constraints.Builder().setRequiresBatteryNotLow(true).build()
            val work = PeriodicWorkRequestBuilder<NeedWaterWorker>(myPlant.wateringInterval.toLong(), TimeUnit.DAYS)
                .setInputData(workDataOf(NAME_OF_PLANT to myPlant.name)).setConstraints(constraints).build()
            if (!isAddDelete) {
                WorkManager.getInstance()
                    .enqueueUniquePeriodicWork(
                        NeedWaterWorker::class.java.name,
                        ExistingPeriodicWorkPolicy.REPLACE, work
                    )
                gardenPlantingRepository.createGardenPlanting(plantId)
            }
            else {
                val gardenPlanting = gardenPlantingRepository.getGardenPlanting(plantId)
                gardenPlantingRepository.removeGardenPlanting(gardenPlanting)
                WorkManager.getInstance().cancelWorkById(work.id)
            }
        }

    }


}
