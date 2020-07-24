
package com.example.mygarden2.utilities

import android.content.Context
import androidx.fragment.app.Fragment
import com.example.mygarden2.data.AppDatabase
import com.example.mygarden2.data.GardenPlantingRepository
import com.example.mygarden2.data.PlantRepository
import com.example.mygarden2.viewmodels.GardenPlantingListViewModelFactory
import com.example.mygarden2.viewmodels.PlantDetailViewModelFactory
import com.example.mygarden2.viewmodels.PlantListViewModelFactory


/**
 * Static methods used to inject classes needed for various Activities and Fragments.
 */
object InjectorUtils {

    private fun getPlantRepository(context: Context): PlantRepository {
        return PlantRepository.getInstance(
                AppDatabase.getInstance(context.applicationContext).plantDao())
    }

    private fun getGardenPlantingRepository(context: Context): GardenPlantingRepository {
        return GardenPlantingRepository.getInstance(
                AppDatabase.getInstance(context.applicationContext).gardenPlantingDao())
    }

    fun provideGardenPlantingListViewModelFactory(
        context: Context
    ): GardenPlantingListViewModelFactory {
        return GardenPlantingListViewModelFactory(getGardenPlantingRepository(context))
    }

    fun providePlantListViewModelFactory(fragment: Fragment): PlantListViewModelFactory {
        return PlantListViewModelFactory(getPlantRepository(fragment.requireContext()), fragment)
    }

    fun providePlantDetailViewModelFactory(
        context: Context,
        plantId: String
    ): PlantDetailViewModelFactory {
        return PlantDetailViewModelFactory(getPlantRepository(context),
                getGardenPlantingRepository(context), plantId)
    }
}
