package com.example.hrrestaurant.data.dataSources.remoteDataSource

import com.google.gson.annotations.SerializedName

//data class ApiResponse(
//    @SerializedName("breakfast")
//    val items:List<MealDto?>,
//
//    )
data class ApiResponse(
    @SerializedName("breakfast")
    val items: List<MealDto>,
):java.io.Serializable