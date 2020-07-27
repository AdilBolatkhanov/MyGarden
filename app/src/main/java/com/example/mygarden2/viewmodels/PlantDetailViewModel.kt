
package com.example.mygarden2.viewmodels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.*
import com.example.mygarden2.App
import com.example.mygarden2.data.GardenPlanting
import com.example.mygarden2.data.GardenPlantingRepository
import com.example.mygarden2.data.Plant
import com.example.mygarden2.data.PlantRepository
import com.example.mygarden2.workers.NeedWaterWorker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * The ViewModel used in [PlantDetailFragment].
 */
const val NAME_OF_PLANT = "NAME_OF_PLANT"
const val ID_OF_PLANT = "ID_OF_PLANT"
class PlantDetailViewModel(
    private val plantRepository: PlantRepository,
    private val gardenPlantingRepository: GardenPlantingRepository,
    private val plantId: String
) : ViewModel() {

    val isPlanted = gardenPlantingRepository.isPlanted(plantId)
    val plant = plantRepository.getPlant(plantId)

    var isNeedWater: Boolean = false

    init {
        viewModelScope.launch(Dispatchers.IO) {
            isNeedWater = gardenPlantingRepository.isPlantNeedWater(plantId)
        }
    }


//    fun addPlantToGarden() {
//        viewModelScope.launch {
//            gardenPlantingRepository.createGardenPlanting(plantId)
//        }
//    }

    var isAddDelete: Boolean = false

    fun doAddDeletePlantToGarden() {
        viewModelScope.launch(Dispatchers.IO) {
            val myPlant = plantRepository.getSpecificPlant(plantId)

            val work =
                PeriodicWorkRequestBuilder<NeedWaterWorker>(myPlant.wateringInterval.toLong(), TimeUnit.MINUTES, myPlant.wateringInterval.toLong()-1, TimeUnit.MINUTES)
                .setInitialDelay(myPlant.wateringInterval.toLong(), TimeUnit.MINUTES)
                .setInputData(workDataOf(NAME_OF_PLANT to myPlant.name , ID_OF_PLANT to plantId))
                .build()

            if (!isAddDelete) {
                WorkManager.getInstance(App.app)
                    .enqueueUniquePeriodicWork(
                        NeedWaterWorker::class.java.name,
                        ExistingPeriodicWorkPolicy.REPLACE, work
                    )
                gardenPlantingRepository.createGardenPlanting(plantId)
            }
            else {
                val gardenPlanting = gardenPlantingRepository.getGardenPlanting(plantId)
                gardenPlantingRepository.removeGardenPlanting(gardenPlanting)
                WorkManager.getInstance(App.app).cancelWorkById(work.id)
            }
        }

    }

    fun updateNeedWater(needWater: Boolean){
        viewModelScope.launch {
            gardenPlantingRepository.updateNeedWaterWithTime(needWater,
                Calendar.getInstance(), plantId)
        }
    }


}
