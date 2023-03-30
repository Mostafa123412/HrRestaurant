package com.example.hrrestaurant.ui.util

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.example.hrrestaurant.R
import com.example.hrrestaurant.ui.activity.main.MainActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.PI

const val ChannelID = "notificationChannel"

// this chanel name appears in the app settings and you can turn it on and off
const val ChannelName = "CHANNEL_NAME"

@AndroidEntryPoint
class MyFirebaseMessagingService() :
    FirebaseMessagingService() {


    //the FCM SDK generates a registration token for the client app instance.
    // If you want to target single devices or create device groups, you'll need to access this token
    /***
     * The registration token may change when:
    The app is restored on a new device
    The user uninstalls/reinstall the app
    The user clears app data.
     */
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
//        sendRegistrationToServer(token)

    }

//    private fun sendRegistrationToServer(token: String) {
//        fireStoreDB.collection("tokens").add("tokens" to token)
//    }

    fun generateNotification(title: String, message: String) {
        val intent = Intent(this, MainActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent
            .getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        var builder = NotificationCompat.Builder(applicationContext, ChannelID)
            .setSmallIcon(R.drawable.hr_logo)
            .setAutoCancel(true)
            .setVibrate(
                longArrayOf(
                    1000,
                    1000,
                    1000,
                    1000
                )
            ) // vibrate for one second , relax for one second
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)

        //attach builder to notification layout
        builder = builder.setContent(getRemoteView(title, message))

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(ChannelID, ChannelName, NotificationManager.IMPORTANCE_HIGH)

            notificationManager.createNotificationChannel(notificationChannel)
        }
        notificationManager.notify(0, builder.build())
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.notification != null) {
            Log.d("FirebaseMessage", "From: ${remoteMessage.from}")
            generateNotification(
                remoteMessage.notification!!.title!!,
                remoteMessage.notification!!.body!!
            )
            Log.d(
                "FirebaseMessage",
                "Message Notification Body: ${remoteMessage.notification!!.body}"
            )

        }
    }

    @SuppressLint("RemoteViewLayout")
    private fun getRemoteView(title: String, message: String): RemoteViews {
        val remoteViews = RemoteViews("com.example.hrrestaurant", R.layout.notification)
        remoteViews.setTextViewText(R.id.title, title)
        remoteViews.setTextViewText(R.id.message, message)
        remoteViews.setImageViewResource(R.id.mainLogo, R.drawable.logo)
        return remoteViews
    }
}