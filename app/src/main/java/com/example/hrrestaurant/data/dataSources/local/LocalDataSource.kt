package com.example.hrrestaurant.data.dataSources.local

import android.util.Log
import androidx.lifecycle.asLiveData
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

    suspend fun deleteAllItems() = dao.deleteItems()
    fun isRoomEmpty(): List<Meal> = dao.getAllItems()

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

    fun getFavouriteItems(): Flow<List<Meal?>> = dao.getFavouriteItems()
    fun addItemToCart(id: Int) {
        CoroutineScope(Dispatchers.IO).launch { dao.addItemToCart(id) }
    }

    fun removeItemFromCart(id: Int) {
        CoroutineScope(Dispatchers.IO).launch { dao.removeItemFromCart(id) }
    }

    fun getAllCartItems(): Flow<List<Meal?>> = dao.getCartItems()


}