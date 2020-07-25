
package com.example.mygarden2.data
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

/**
 * The Data Access Object for the [GardenPlanting] class.
 */
@Dao
interface GardenPlantingDao {
    @Query("SELECT * FROM garden_plantings")
    fun getGardenPlantings(): LiveData<List<GardenPlanting>>

    @Query("SELECT EXISTS(SELECT 1 FROM garden_plantings WHERE plant_id = :plantId LIMIT 1)")
    fun isPlanted(plantId: String): LiveData<Boolean>

    /**
     * This query will tell Room to query both the [Plant] and [GardenPlanting] tables and handle
     * the object mapping.
     */
    @Transaction
    @Query("SELECT * FROM plants WHERE id IN (SELECT DISTINCT(plant_id) FROM garden_plantings)")
    fun getPlantedGardens(): LiveData<List<PlantAndGardenPlantings>>

    @Query("SELECT * FROM garden_plantings WHERE plant_id = :plantId")
    suspend fun getGardenPlanting(plantId: String): GardenPlanting

    @Query("SELECT need_water FROM garden_plantings WHERE plant_id = :plantId")
    fun isPlantNeedWater(plantId: String): LiveData<Boolean>

    @Query("UPDATE garden_plantings SET need_water = :needWater WHERE plant_id = :plantId")
    suspend fun updateNeedWater(needWater: Boolean, plantId: String)

    @Query("UPDATE garden_plantings SET need_water = :needWater,last_watering_date = :time WHERE plant_id = :plantId")
    suspend fun updateNeedWaterWithTime(needWater: Boolean, time: Long, plantId: String)

    @Query("DELETE FROM garden_plantings")
    suspend fun clearGarden()

    @Insert
    suspend fun insertGardenPlanting(gardenPlanting: GardenPlanting): Long

    @Delete
    suspend fun deleteGardenPlanting(gardenPlanting: GardenPlanting)
}