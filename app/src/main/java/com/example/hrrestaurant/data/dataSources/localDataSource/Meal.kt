package com.example.hrrestaurant.data.dataSources.localDataSource

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
    val estimatedTime: String,
    var price: Double,
    val category: String?,
    val topRated: Int?,
    var isChecked: Boolean,
    var isAddedToChart: Boolean,
    var rate:Float? = 0F,
    var count:Int = 0,
)
