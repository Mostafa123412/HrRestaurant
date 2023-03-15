package com.example.hrrestaurant.data.dataSources.local

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Meal(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val title: String?,
    val description: String?,
    var itemImage: Bitmap?,
    val estimatedTime: Double,
    var price: Double,
    val category: String?,
    val topRated: Int?,
    var isChecked: Boolean,
    var isAddedToChart: Boolean,
    var rate:Float? = 0F,
    var count:Int = 0,
)
