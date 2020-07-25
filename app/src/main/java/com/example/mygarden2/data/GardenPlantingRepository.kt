
package com.example.mygarden2.data
class GardenPlantingRepository private constructor(
    private val gardenPlantingDao: GardenPlantingDao
) {

    suspend fun createGardenPlanting(plantId: String) {
        val gardenPlanting = GardenPlanting(plantId)
        gardenPlantingDao.insertGardenPlanting(gardenPlanting)
    }

    suspend fun removeGardenPlanting(gardenPlanting: GardenPlanting) {
        gardenPlantingDao.deleteGardenPlanting(gardenPlanting)
    }

    suspend fun getGardenPlanting(plantId: String): GardenPlanting = gardenPlantingDao.getGardenPlanting(plantId)

    // AS (must be added to tests)
    suspend fun clearGarden() = gardenPlantingDao.clearGarden()

    fun isPlanted(plantId: String) =
            gardenPlantingDao.isPlanted(plantId)

    fun isPlantNeedWater(plantId: String) = gardenPlantingDao.isPlantNeedWater(plantId)

    fun getPlantedGardens() = gardenPlantingDao.getPlantedGardens()

    suspend fun updateNeedWater(needWater: Boolean, plantId: String) = gardenPlantingDao.updateNeedWater(needWater, plantId)

    suspend fun updateNeedWaterWithTime(needWater: Boolean,time: Long, plantId: String) = gardenPlantingDao.updateNeedWaterWithTime(needWater,time, plantId)

    companion object {

        // For Singleton instantiation
        @Volatile private var instance: GardenPlantingRepository? = null

        fun getInstance(gardenPlantingDao: GardenPlantingDao) =
                instance ?: synchronized(this) {
                    instance ?: GardenPlantingRepository(gardenPlantingDao).also { instance = it }
                }
    }
}