package com.example.hrrestaurant.data.dataSources.local

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {
    @Query(
        "UPDATE orderTable SET orderStatus = :orderStatus WHERE orderRemoteId = :orderId"
    )
    suspend fun changeOrderStatus(orderStatus: String, orderId: String)

    @Query("SELECT * FROM meal WHERE id IN (:mealId) ")
    suspend fun getMealDetails(mealId: Array<Int>):List<Meal?>

    @Query("Select orderStatus From orderTable Where orderRemoteId = :orderId")
    fun getOrderStatus(orderId: String):Flow<String?>
    //    @Query("ALTER TABLE orderIdTable ADD orderId :orderId")

    @Query("SELECT orderRemoteId From orderTable")
    suspend fun getUsersOrdersId(): List<String>
    @Insert(onConflict = OnConflictStrategy.REPLACE , entity = Order::class)
    fun insertOrder(order: Order)

    @Query("Select * From orderTable Where userId = :userId")
    fun getOrdersByUserId(userId: String): Flow<List<Order>>

    @Query("Select * From meal WHERE category = :category")
    fun fetchItemsByCategory(category: String): Flow<List<Meal?>?>

    @Query("SELECT title FROM meal WHERE id = :mealId")
    fun getMealTitleByMealId(mealId: Int): String

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: Meal?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(list: List<Meal?>)

    @Query("Select COUNT(*) from meal")
    fun getAllItems(): Flow<Int?>

    @Query("SELECT (SELECT COUNT(*) FROM meal) == 0")
    suspend fun isRoomEmpty(): Boolean?

    @Query("SELECT COUNT(*) FROM meal")
    fun orderTableRowNumbers(): Flow<Int?>

    @Query("Select * from meal where topRated = 1")
    fun getTopRatedMeals(): Flow<List<Meal?>?>

    /***
     * favourite is the status to be updated
     */
    @Query("UPDATE meal SET isChecked = 1 WHERE id = :id")
    suspend fun addItemToFavourite(id: Int?)

    @Query("UPDATE meal SET isChecked = 0 WHERE id = :id")
    suspend fun removeItemFromFavourite(id: Int?)

    @Query("UPDATE meal SET isAddedToChart = 1 WHERE id = :id")
    suspend fun addItemToCart(id: Int?)

    @Query("UPDATE meal SET isAddedToChart = 0 WHERE id = :id")
    suspend fun removeItemFromCart(id: Int?)

    @Query("UPDATE meal SET count = count + 1 WHERE id = :id")
    suspend fun incrementItemCount(id: Int)

    @Query("UPDATE meal SET count = count - 1 WHERE id = :id")
    suspend fun decrementItemCount(id: Int)

    @Query("UPDATE meal SET count = 0 WHERE id = :id")
    fun setItemCountToZero(id: Int)

    @Query("SELECT * FROM meal WHERE isChecked = 1")
    fun getFavouriteItems(): Flow<List<Meal?>>

    @Query("SELECT * FROM meal WHERE isAddedToChart = 1")
    fun getCartItems(): Flow<List<Meal?>>

    // || is string concatenate operator. Think of it as + in Java String
    // SQLite uses single quotation marks to represent string literals
    // https://stackoverflow.com/questions/44184769/android-room-select-query-with-like
    @Query("Select * from meal where category Like '%' || :searchText || '%' OR title LIKE '%' || :searchText || '%' OR description LIKE '%' || :searchText || '%' ")
    fun getItemsBySearchText(searchText: String): Flow<List<Meal?>>


//    @Query("DELETE FROM Tasks WHERE completed = 1")
//    suspend fun deleteCompletedTasks(): Int
//    @Query("DELETE FROM Tasks WHERE entryid = :taskId")
//    suspend fun deleteTaskById(taskId: String): Int


}