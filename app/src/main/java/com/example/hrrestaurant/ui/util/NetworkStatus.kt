package com.example.hrrestaurant.ui.util

import android.content.Context
import android.net.ConnectivityManager

object NetworkStatus {
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetwork = connectivityManager.activeNetworkInfo

        return activeNetwork != null && activeNetwork.isConnected
    }
}