package com.example.hrrestaurant.ui

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

//Above step is needed because the application context is provided through generated application class
@HiltAndroidApp
class App: Application() {
//    override fun onCreate() {
//        super.onCreate()
////        Objects.createApiInstance()
//        MyDatabase.getDatabaseInstance(context = applicationContext)
//    }
}