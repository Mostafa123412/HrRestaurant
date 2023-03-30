package com.example.hrrestaurant.ui.base

import com.example.hrrestaurant.data.dataSources.local.Order

interface OrderListener {
    fun orderAgain(order: Order)
    fun moreDetails(mealsId: List<Int>)
    fun cancelOrder(orderId: String)
    fun addItemsToCartAgain(itemsId:List<Int>)
    fun removeAllItemsFromCart(itemsId:List<Int>)
    suspend fun getMealTitleByMealId(mealId: Int):String
}