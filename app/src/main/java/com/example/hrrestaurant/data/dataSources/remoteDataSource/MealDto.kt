package com.example.hrrestaurant.data.dataSources.remoteDataSource

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

//Parcelable is more faster than serializable
@Parcelize
data class MealDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name_en")
    val title: String?,
    @SerializedName("descrption_en")
    val description: String?,
    @SerializedName("image")
    var itemImage: String,
    @SerializedName("time")
    val estimatedTime: String,
    @SerializedName("price")
    var price: Double,
    @SerializedName("category")
    val category: String?,
    @SerializedName("topRated")
    val topRated: Int?,
):Parcelable