package com.example.hrrestaurant.ui.util

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging

object Constants {
    const val youtube_api_key = "AIzaSyA_NVCILTla1yIIvzWUAI_I_H4VqcRrVno"
    const val DIRECTION_SDK_KEY = "AIzaSyBJD_29Jnl4mz1TeZuzGmHyTqMKzvZ7ArE"
    const val BASE_URL = "https://breakfastapi.000webhostapp.com/api/"
    const val LOGIN = 1
    const val SIGNUP = 2
    const val USERID = "user_id"
    const val ChannelID = "notificationChannel"
    const val ChannelName = "CHANNEL_NAME"
    lateinit var TOKEN: String

    init {
        getToken()
    }

    private fun getToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("MESSEGING", "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }
            // Get new FCM registration token
            TOKEN = task.result
        }
    }
}