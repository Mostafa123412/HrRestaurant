package com.example.hrrestaurant.data.dataSources.local

import android.util.Log
import androidx.lifecycle.asLiveData
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val dao: Dao) {

    //    private val dao = db.daoInstance()
    fun getItemByCategory(category: String): Flow<List<Meal?>?> {
        return dao.fetchItemsByCategory(category)
    }

    fun getItemBySearchText(searchText: String): Flow<List<Meal?>> {
        return dao.getItemsBySearchText(searchText)
    }

    fun getTopRatedItems(): Flow<List<Meal?>?> {
        return dao.getTopRatedMeals()
    }

    suspend fun insertItemsIntoLocalStorage(list: List<Meal?>) {
        Log.d("Repository", "Trying to cache $list")
        dao.insertItems(list)
    }

    suspend fun addItem(item: Meal?) {
        Log.d("Repository", "Trying to cache ${item?.id} ${item?.title}")
        withContext(Dispatchers.IO) {
            dao.insertItem(item)
        }
    }

    fun isRoomEmpty(): Flow<Int?> {

    return dao.getAllItems()
    }

    suspend fun addItemToFavourite(id: Int?) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.addItemToFavourite(id)
        }
    }

    suspend fun removeItemFromFavourite(id: Int?) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.removeItemFromFavourite(id)
        }
    }
    suspend fun getMealTitleByMealId(mealId:Int):String = dao.getMealTitleByMealId(mealId)


    fun getFavouriteItems(): Flow<List<Meal?>> = dao.getFavouriteItems()
    suspend fun addItemToCart(id: Int) {
        CoroutineScope(Dispatchers.IO).launch { dao.addItemToCart(id) }
    }

    suspend fun removeItemFromCart(id: Int) {
        CoroutineScope(Dispatchers.IO).launch { dao.removeItemFromCart(id) }
    }

    suspend fun incrementItemCount(id: Int) {
        dao.incrementItemCount(id)
    }
    suspend fun decrementItemCount(id: Int) = dao.decrementItemCount(id)

    fun getAllCartItems(): Flow<List<Meal?>> = dao.getCartItems()
    suspend fun setItemCountToZero(id: Int) {
        dao.setItemCountToZero(id)
    }

    suspend fun changeOrderStatus(orderStatus:String , orderRemoteId: String) = dao.changeOrderStatus(orderStatus , orderRemoteId)
    suspend fun insertOrder(order:Order) = dao.insertOrder(order)
    suspend fun getOrdersByUserId(userId:String):Flow<List<Order>> = dao.getOrdersByUserId(userId)
    suspend fun getAllOrders():Flow<List<Order>> {
        return dao.getAllOrders()
    }


}