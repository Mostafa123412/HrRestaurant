package com.example.hrrestaurant.ui.base

interface OrderListener {
    fun orderAgain(orderId:String)
    fun moreDetails(orderId: String)
    fun cancelOrder(orderId: String)
    suspend fun getMealTitleByMealId(mealId: Int, onTitleFound: (String) -> Unit)
}