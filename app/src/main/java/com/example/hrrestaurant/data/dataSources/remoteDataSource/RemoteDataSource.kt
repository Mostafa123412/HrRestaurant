package com.example.hrrestaurant.data.dataSources.remoteDataSource

import android.util.Log
import com.example.hrrestaurant.ui.util.NetworkResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val api: HrApi) {



    suspend fun getDataFromNetwork(): NetworkResponse<ApiResponse?> {
        Log.d("Repository", "Now Entering RemoteDataSource ............")
//        val gson = GsonBuilder()
//            .setLenient()
//            .create()
        return withContext(Dispatchers.IO) {
            try {
                Log.d("Repository ", "api Instance  ${api.toString()}")
                var result = api.fetchBreakfast()
                Log.d("Repository", "Result is here $result")

                if (result.isSuccessful) {
                    Log.d("Repository ", "Data ${result.body()?.items}")
                    NetworkResponse.Success(result.body())
                } else {
                    NetworkResponse.Error(
                        errorMessage ="result is back but not successful"+ result.errorBody()?.toString() ?: "Something went Wrong"
                    )
                }
            } catch (e: HttpException) {
                NetworkResponse.Error(errorMessage = "Http Exception \n ${e.message} ")
            } catch (e: IOException) {
                NetworkResponse.Error("IO Exception \n${e.message}")
            } catch (e: Exception) {
                NetworkResponse.Error(errorMessage = "Exception  \n ${e.message} ")
            }
        }
    }
//    suspend fun getDataFromNetwork(): NetworkResponse<ApiResponse> {
//        return safeApiCall { api.fetchBreakfast() }
//    }


    // we'll use this function in all
    // repos to handle api errors.
    private suspend fun <T> safeApiCall(apiToBeCalled: suspend () -> Response<T>): NetworkResponse<T> {
        // Returning api response
        // wrapped in Resource class
        return withContext(Dispatchers.IO) {
            try {
                // Here we are calling api lambda
                // function that will return response
                // wrapped in Retrofit's Response class
                Log.d("Repository", " Remote DataSource Trying to get data from internet ")

                val response: Response<T> = apiToBeCalled()
                Log.d("Repository", " Remote DataSource data is here  ")
                if (response.isSuccessful) {
                    // In case of success response we
                    // are returning Resource.Success object
                    // by passing our data in it.
                    NetworkResponse.Success(data = response.body()!!)
                } else {
                    NetworkResponse.Error(
                        errorMessage = "Error response is not successufl \n "+response.errorBody()?.toString().plus(response.message()) ?: "Something went wrong"
                    )
                }

            } catch (e: HttpException) {
                // Returning HttpException's message
                // wrapped in Resource.Error
                NetworkResponse.Error("Http Exception "+ e.message ?: "Something went wrong")
            } catch (e: IOException) {
                // Returning no internet message
                // wrapped in Resource.Error
                NetworkResponse.Error( "IO Exception \n  ${e.message} \n ${e.localizedMessage}")
            } catch (e: Exception) {
                // Returning 'Something went wrong' in case
                // of unknown error wrapped in Resource.Error
                NetworkResponse.Error(errorMessage = "Exception")
            }
        }
    }

}