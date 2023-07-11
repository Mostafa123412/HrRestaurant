package com.example.hrrestaurant.ui.util

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging

object Constants {
    const val youtube_api_key = "AIzaSyA_NVCILTla1yIIvzWUAI_I_H4VqcRrVno"
    const val DIRECTION_SDK_KEY = "AIzaSyBJD_29Jnl4mz1TeZuzGmHyTqMKzvZ7ArE"
    const val BASE_URL: String = "https://breakfastttapi.000webhostapp.com/api/"
    const val API_TOKEN = "Bearer a30FtE7bVefxUD9q5KPKpGSZuJPVeh5p8d5ZlxdPI4vlwKnfz0ECUlZKIvlj-lFIZFAc-h8Uw_itb15IHpXct5-CD9tL1Sfdu84o3HwfuUX_AynFoYDVjj4w5Xgg5f1ROfJx0Cc35AGPezMGU3pa0hImAhtefkrfWei4A5Gh8OIf_H8oX4QPDaUehfJ-Rq1rawDALWrnI9gW2nx6H6TGZeXiSaV_gUm1_jq4pHruIms-U7k2LC7u9x8Wy_GD6zBkr9_wDj0xPAy4Gnyd_1Z2uHk900A4J-lpkA3TnpGqc8EFaMIMG7WILh8zEyrzCosi6JsbG1ZSK8cxD12RTJ91jHa-E9exPGp_y7A8maTr_JwDjAb-oPl1UYZLXvTnorGDS0PBfzyl4LyOQcxnKRZWRPdKL5gq50QJv4mKGYZGBiewp3XKUeCZdYPrsx_JlUEq4QpN9THqb0xTYed--ZOs4PvaONBM7DOQzC80o_RQZQjSRYZQIVUqtjzjpTwsKZ76"

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