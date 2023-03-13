package com.example.hrrestaurant.data.dataSources.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {

    @Query("Select * From meal WHERE category = :category")
    fun fetchItemsByCategory(category: String): Flow<List<Meal?>?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: Meal?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(list: List<Meal?>)

    @Query("Select * from meal")
    fun getAllItems(): List<Meal>

    @Query("SELECT (SELECT COUNT(*) FROM meal) == 0")
    suspend fun isRoomEmpty(): Boolean?

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

    @Query("DELETE FROM meal")
    suspend fun deleteItems()

    @Query("SELECT * FROM meal WHERE isChecked = 1")
    fun getFavouriteItems(): Flow<List<Meal?>>

    @Query("SELECT * FROM meal WHERE isAddedToChart = 1")
    fun getCartItems(): Flow<List<Meal?>>

    // || is string concatenate operator. Think of it as + in Java String
    // SQLite uses single quotation marks to represent string literals
    // https://stackoverflow.com/questions/44184769/android-room-select-query-with-like
    @Query("Select * from meal where category Like '%' || :searchText || '%' OR title LIKE '%' || :searchText || '%' OR description LIKE '%' || :searchText || '%' ")
    fun getItemsBySearchText(searchText:String):Flow<List<Meal?>>

//    @Query("DELETE FROM Tasks WHERE completed = 1")
//    suspend fun deleteCompletedTasks(): Int
//    @Query("DELETE FROM Tasks WHERE entryid = :taskId")
//    suspend fun deleteTaskById(taskId: String): Int


}