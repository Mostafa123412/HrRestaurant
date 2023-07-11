package com.example.hrrestaurant.data.dataSources.remoteDataSource

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

//Parcelable is more faster than serializable
/***
 * @Parcelize. This annotation is used to automatically generate the necessary code to make the class Parcelable,
 * which means that its instances can be easily passed between different components of an Android application.
 */
//@Parcelize
data class MealDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name_en")
    val title: String?,
    @SerializedName("descrption_en")
    val description: String?,
    @SerializedName("name_ar")
    val nameAr:String?,
    @SerializedName("descrption_ar")
    val descriptionAr:String?,
    @SerializedName("image")
    var itemImage: String,
    @SerializedName("time")
    val estimatedTime: String,
    @SerializedName("category")
    val category: String?,
    @SerializedName("topRated")
    val topRated: Int?,
    @SerializedName("price")
    var price: Double,
) : java.io.Serializable