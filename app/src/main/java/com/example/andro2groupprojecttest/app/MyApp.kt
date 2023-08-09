package com.example.andro2groupprojecttest

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class MyApp : Application() {

    companion object{
        const val CHANNEL_ID = "MyNotificationChannel"
    }
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel(){
       if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
           val notificationChannel = NotificationChannel(CHANNEL_ID,"My Channel",NotificationManager.IMPORTANCE_DEFAULT)
           val manager = getSystemService(NotificationManager::class.java)
           manager.createNotificationChannel(notificationChannel)
       }
    }
}