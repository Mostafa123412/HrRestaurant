package com.example.hrrestaurant.ui

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.example.hrrestaurant.ui.util.ChannelID
import com.example.hrrestaurant.ui.util.ChannelName
import com.example.hrrestaurant.ui.util.Constants
import com.example.hrrestaurant.ui.util.MyFirebaseMessagingService
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp

//Above step is needed because the application context is provided through generated application class
@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
//        Objects.createApiInstance()
        Constants
        FirebaseApp.initializeApp(this)
//        createNotificationChannel()
        MyFirebaseMessagingService()
    }

    private fun createNotificationChannel() {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(ChannelID, ChannelName, NotificationManager.IMPORTANCE_HIGH)

            notificationChannel.description = "Used to notify the user with any new order state"

            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}