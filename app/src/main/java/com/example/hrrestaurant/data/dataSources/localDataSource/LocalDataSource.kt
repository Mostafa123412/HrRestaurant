package com.example.hrrestaurant.data.dataSources.localDataSource

import android.util.Log
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val dao: Dao) {

     fun getOrderStatus(orderId: String):Flow<String?> = dao.getOrderStatus(orderId)

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

    suspend fun getMealTitleByMealId(mealId: Int): String = dao.getMealTitleByMealId(mealId)


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

    suspend fun changeOrderStatus(orderStatus: String, orderId: String) {
        dao.changeOrderStatus(orderStatus, orderId)
        Log.d("Firebase", "state is $orderStatus in local dataSource")
    }

    suspend fun getUsersOrdersId(): List<String> = dao.getUsersOrdersId()

    suspend fun insertOrder(order: Order) {
        coroutineScope { dao.insertOrder(order) }
    }

    suspend fun getMealDetails(mealId: Array<Int>):List<Meal?> {
        Log.d("Firebase", "Dao returned ${dao.getMealDetails(mealId)}")
        return dao.getMealDetails(mealId)
    }

    fun getOrdersByUserId(userId: String): Flow<List<Order>> = dao.getOrdersByUserId(userId)

    fun orderTableRowNumbers(): Flow<Int?> = dao.orderTableRowNumbers()

}