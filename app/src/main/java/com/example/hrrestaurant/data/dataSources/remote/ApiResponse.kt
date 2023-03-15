package com.example.hrrestaurant.data.dataSources.remote

import com.google.gson.annotations.SerializedName

data class ApiResponse(
    @SerializedName("breakfast")
    val items:List<MealDto?>,

    )
