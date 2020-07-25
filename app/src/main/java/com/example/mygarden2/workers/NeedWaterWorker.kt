package com.example.mygarden2.workers

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.example.mygarden2.GardenActivity
import com.example.mygarden2.R
import com.example.mygarden2.data.GardenPlantingDao
import com.example.mygarden2.data.GardenPlantingRepository
import com.example.mygarden2.viewmodels.ID_OF_PLANT
import com.example.mygarden2.viewmodels.NAME_OF_PLANT
import java.lang.Math.random


class NeedWaterWorker(val context: Context,
                      workerParams: WorkerParameters,
                      private val gardenPlantingRepository: GardenPlantingRepository
) : CoroutineWorker(context, workerParams){


    override suspend fun doWork(): Result {
        createChannel()
        if (inputData.getString(ID_OF_PLANT) != null) {
            gardenPlantingRepository.updateNeedWater(true, inputData.getString(ID_OF_PLANT)!!)

            val intent = Intent(context, GardenActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                .setContentTitle(inputData.getString(NAME_OF_PLANT))
                .setContentText("It's time to water the ${inputData.getString(NAME_OF_PLANT)}")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

            with(NotificationManagerCompat.from(context)) {
                // notificationId is a unique int for each notification that you must define
                notify(inputData.getString(NAME_OF_PLANT)!!.length, builder.build())
            }

            return Result.success()
        }else{
            return Result.failure()
        }
    }



    class Factory(private val repository: GardenPlantingRepository): WorkerFactory() {
        override fun createWorker(
            appContext: Context,
            workerClassName: String,
            workerParameters: WorkerParameters
        ): ListenableWorker? {
            return NeedWaterWorker(appContext, workerParameters, repository)
        }

    }


    private fun createChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            val name = "Water_notification"
            val description = "Water_notification"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = description

            // Add the channel
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

            notificationManager?.createNotificationChannel(channel)
        }
    }
    companion object{
        const val CHANNEL_ID = "channel_3"
    }

}