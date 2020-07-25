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
import androidx.work.WorkerParameters
import com.example.mygarden2.GardenActivity
import com.example.mygarden2.R
import com.example.mygarden2.viewmodels.NAME_OF_PLANT
import java.lang.Math.random


class NeedWaterWorker(val context: Context,
                      workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams){
    private var notificationId = inputData.getString(NAME_OF_PLANT)?.length ?: (100 * random()).toInt()

    override suspend fun doWork(): Result {
        createChannel()

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
            notify(notificationId, builder.build())
        }


        return Result.success()
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