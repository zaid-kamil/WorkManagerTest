package com.example.workmanagertest

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val notificationChannel = NotificationChannel(
            "work_manager_test",
            "Work",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(notificationChannel)
    }
}