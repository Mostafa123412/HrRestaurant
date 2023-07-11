package com.example.hrrestaurant.data.dataSources.remoteDataSource

import retrofit2.Response
import retrofit2.http.GET


interface HrApi {
    /***
     * Headers are part of the HTTP protocol and are used to send additional information in a request or response.
     * They are used to transmit metadata about the message,
     * such as the content type of the message body, the encoding of the message,
     * and authentication information.
     */
    @GET("api/breakfast")
    suspend fun fetchBreakfast(): Response<ApiResponse?>


}