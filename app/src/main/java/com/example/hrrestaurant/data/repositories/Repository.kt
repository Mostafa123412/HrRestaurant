package com.example.hrrestaurant.data.repositories

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import androidx.lifecycle.LiveData
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.hrrestaurant.data.dataSources.local.Meal
import com.example.hrrestaurant.data.dataSources.local.LocalDataSource
import com.example.hrrestaurant.data.dataSources.local.MealMapper
import com.example.hrrestaurant.data.dataSources.local.Order
import com.example.hrrestaurant.data.dataSources.remote.RemoteDataSource
import com.example.hrrestaurant.data.dataSources.remote.User
import com.example.hrrestaurant.ui.util.NetworkResponse
import com.example.hrrestaurant.ui.util.UiState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.internal.cache.CacheInterceptor
import javax.inject.Inject

class Repository @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val mealMapper: MealMapper,
) {
    fun getOrderStatus(orderId: String):Flow<String?> {

        var orderStatus = localDataSource.getOrderStatus(orderId)
        Log.d("Firebase", "previous order status is  = $orderStatus")
        return orderStatus

    }


    fun orderTableRowNumbers(): Flow<Int?> = localDataSource.orderTableRowNumbers()

    //Orders
    suspend fun changeOrderStatus(orderStatus: String, orderRemoteId: String) {
        localDataSource.changeOrderStatus(orderStatus, orderRemoteId)
        Log.d("Firebase", "state is $orderStatus in Repository")
    }


    suspend fun getMealTitleByMealId(mealId: Int): String =
        localDataSource.getMealTitleByMealId(mealId)

    suspend fun insertOrder(order: Order) {
        coroutineScope { localDataSource.insertOrder(order) }
    }

    fun getOrdersByUserId(userId: String): Flow<List<Order>> =
        localDataSource.getOrdersByUserId(userId)


    fun isRoomEmpty(): Flow<Boolean> = flow {
        localDataSource.isRoomEmpty().collect {
            Log.d("Repository", "Room status = $it")
            if (it == null || it == 0) emit(true) else emit(false)
        }
    }

    fun getItemBySearchText(searchText: String): Flow<List<Meal?>> =
        localDataSource.getItemBySearchText(searchText)

    suspend fun pushRate(rate: Double): Flow<UiState> = flow {
        emit(UiState.Loading)
    }

    fun getDataFromNetworkAndCacheIt(context: Context): Flow<UiState> = flow<UiState> {
        Log.d("Repository", "Trying to get data ............")
        emit(UiState.Loading)
        when (val dataFromNetwork = remoteDataSource.getDataFromNetwork()) {
            is NetworkResponse.Success -> {
                withContext(Dispatchers.IO) {
                    val data = dataFromNetwork.data?.items!!
                    val listOfMeals = mealMapper.mapList(data, context = context)
                    localDataSource.insertItemsIntoLocalStorage(listOfMeals)
                    Log.d("Repository", "Success getDataFromInternetAndCacheIt")
                }
                emit(UiState.Success)
            }
            is NetworkResponse.Error -> emit(UiState.Error(dataFromNetwork.errorMessage))
            else -> emit(UiState.Loading)
        }
    }

    private suspend fun getBitmap(imgUrl: String, context: Context): Bitmap {
        val loading = ImageLoader(context)
        val request = ImageRequest.Builder(context)
            .data(imgUrl).build()
        val result = (loading.execute(request = request) as SuccessResult).drawable
        return (result as BitmapDrawable).bitmap
    }
//
//    suspend fun <T> getDataFromInternetAndCacheIt(): NetworkResponse<T> {
//        try {

    //            withContext(Dispatchers.IO) {
//                val result = remoteDataSource.getDataFromNetwork()
//                result?.forEach { meal ->
//                    localDataSource.addItem(meal)
//                }
//            }
//            Log.d("Repository", "cached successfully")
//            return true
//        } catch (e: Exception) {
//            Log.d("Repository", "getDataFromInternet: Failed ${e.message}")
//        }
//        return false
//    }
    suspend fun getMealDetails(mealId: Array<Int>): List<Meal?> = localDataSource.getMealDetails(mealId)


    //Home page
    fun getTopRated(): Flow<List<Meal?>?> =
        localDataSource.getTopRatedItems()


    fun getMostPopular(): Flow<List<Meal?>?> =
        localDataSource.getItemByCategory("mostPopular")


    // Breakfast
    fun getCroissant(): Flow<List<Meal?>?> {
        return localDataSource.getItemByCategory("croissant ")
    }

    fun getGeneral(): Flow<List<Meal?>?> {
        return localDataSource.getItemByCategory("general")
    }

    //
    fun getOmelette(): Flow<List<Meal?>?> {
        return localDataSource.getItemByCategory("omelette")
    }

    fun getPancake(): Flow<List<Meal?>?> = localDataSource.getItemByCategory("pancake")

    //Lunch Fragment
    fun getSoup(): Flow<List<Meal?>?> = localDataSource.getItemByCategory("soup")
    fun getAppetizers(): Flow<List<Meal?>?> = localDataSource.getItemByCategory("appetizers")
    fun getSalades(): Flow<List<Meal?>?> = localDataSource.getItemByCategory("salade")
    fun getBurger(): Flow<List<Meal?>?> = localDataSource.getItemByCategory("burger")
    fun getSandwiches(): Flow<List<Meal?>?> = localDataSource.getItemByCategory("sandwiches")
    fun getPasta(): Flow<List<Meal?>?> = localDataSource.getItemByCategory("pasta")
    fun getPizza(): Flow<List<Meal?>?> = localDataSource.getItemByCategory("pizza")
    fun getChicken(): Flow<List<Meal?>?> = localDataSource.getItemByCategory("chicken")
    fun getBeef(): Flow<List<Meal?>?> = localDataSource.getItemByCategory("beef")
    fun getMixDishes(): Flow<List<Meal?>?> = localDataSource.getItemByCategory("mixDishes")
    fun getSeaFood(): Flow<List<Meal?>?> = localDataSource.getItemByCategory("seaFood")
    fun getSauces(): Flow<List<Meal?>?> = localDataSource.getItemByCategory("sauces")
    fun getRibEyeSteak(): Flow<List<Meal?>?> = localDataSource.getItemByCategory("ribEyeSteak")
    fun getFajitaDishes(): Flow<List<Meal?>?> =
        localDataSource.getItemByCategory("fajitaDishes")

    fun getSideDishes(): Flow<List<Meal?>?> = localDataSource.getItemByCategory("sideDishes")


    suspend fun addItemToFavourite(id: Int) {
        coroutineScope { localDataSource.addItemToFavourite(id) }
    }

    suspend fun removeItemFromFavourite(id: Int) {
        coroutineScope { localDataSource.removeItemFromFavourite(id) }
    }

    suspend fun addItemToCart(id: Int) {
        coroutineScope { localDataSource.addItemToCart(id) }
    }

    fun getAllCartItems(): Flow<List<Meal?>> = localDataSource.getAllCartItems()

    suspend fun getUsersOrdersId(): List<String> = localDataSource.getUsersOrdersId()

    suspend fun removeItemFromCart(id: Int) {
        coroutineScope {
            localDataSource.removeItemFromCart(id)
        }
    }

    suspend fun incrementItemCount(id: Int) {
        coroutineScope { localDataSource.incrementItemCount(id) }
    }

    suspend fun decrementItemCount(id: Int) {
        coroutineScope { localDataSource.decrementItemCount(id) }
    }


    fun getFavouriteItems(): Flow<List<Meal?>> = localDataSource.getFavouriteItems()

    // Desserts
    fun getHotDrinks(): Flow<List<Meal?>?> = localDataSource.getItemByCategory("hotDrinks")
    fun getTurkishCoffee(): Flow<List<Meal?>?> = localDataSource.getItemByCategory("turkishCoffee ")
    fun getClassicCoffee(): Flow<List<Meal?>?> = localDataSource.getItemByCategory("classicCoffee")
    fun getMediumCoffee(): Flow<List<Meal?>?> = localDataSource.getItemByCategory("mediumCoffee")
    fun getMilkShakes(): Flow<List<Meal?>?> = localDataSource.getItemByCategory("milkShakes")
    fun getFreshJuices(): Flow<List<Meal?>?> = localDataSource.getItemByCategory("freshJuices")
    fun getIcedCoffee(): Flow<List<Meal?>?> = localDataSource.getItemByCategory("icedCoffee")
    fun getFrappueeino(): Flow<List<Meal?>?> = localDataSource.getItemByCategory("frappueeino")
    fun getCocktails(): Flow<List<Meal?>?> = localDataSource.getItemByCategory("cocktails")
    fun getSmoothies(): Flow<List<Meal?>?> = localDataSource.getItemByCategory("smoothies")
    fun getYogurt(): Flow<List<Meal?>?> = localDataSource.getItemByCategory("yogurt")
    suspend fun notifyServerByUserLogin(user: User) {
        remoteDataSource.notifyServerWithUserLogin(user)
    }

    fun getIceCream(): Flow<List<Meal?>?> = localDataSource.getItemByCategory("iceCream")

    //should i here change the scope to IO ?
    suspend fun setItemCountToZero(id: Int) {
        coroutineScope { localDataSource.setItemCountToZero(id) }
    }

//    suspend fun postItemRate(id: Int?, rate: Float?): String {
//        val postRateResult = remoteDataSource.postItemRate(id, rate)
//        when (postRateResult) {
//            Log.d("Repository", "post result ${postRateResult.} to favourite....")
//                    is NetworkResponse.Error -> return postRateResult.errorMessage
//            NetworkResponse.Loading -> TODO()
//            is NetworkResponse.Success -> return "succeeded"
//        }
//    }


}
