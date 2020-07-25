package com.example.mygarden2

import android.app.Application
import android.content.Context
import androidx.work.Configuration
import androidx.work.WorkManager
import com.example.mygarden2.data.AppDatabase
import com.example.mygarden2.utilities.InjectorUtils
import com.example.mygarden2.workers.NeedWaterWorker

class App:Application() {

    companion object{
        lateinit var app:Context
            private set
    }

    override fun onCreate() {
        super.onCreate()
        app = this
        val workManagerConfiguration = Configuration.Builder()
            .setWorkerFactory(NeedWaterWorker.Factory(InjectorUtils.getGardenPlantingRepository(this)))
            .build()
        WorkManager.initialize(this, workManagerConfiguration)
    }
}