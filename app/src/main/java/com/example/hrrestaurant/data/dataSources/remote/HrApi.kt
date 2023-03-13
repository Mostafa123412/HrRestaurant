package com.example.hrrestaurant.data.dataSources.remote

import retrofit2.Response
import retrofit2.http.GET


interface HrApi {

@GET ("breakfast")
suspend fun fetchBreakfast (): Response<ApiResponse>

//@POST("rate")
//suspend fun pushRate(
//    @Body itemId:Int,
//    @Body rate:Float
//):Response<>

}