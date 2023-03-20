package com.example.hrrestaurant.ui

import android.app.Application
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp

//Above step is needed because the application context is provided through generated application class
@HiltAndroidApp
class App: Application() {
    override fun onCreate() {
        super.onCreate()
//        Objects.createApiInstance()
        FirebaseApp.initializeApp(this)
    }
}