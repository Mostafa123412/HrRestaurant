package com.example.hrrestaurant.ui.base

import com.example.hrrestaurant.data.dataSources.localDataSource.Order

interface OrderListener {
    fun orderSameOrderAgain(order: Order)


    fun addThisOrderItemsToCartAgain(itemsId:List<Int>)
    fun removeOrderItemsFromCart(itemsId:List<Int>)

    fun moreDetails(mealsId: List<Int>)
    fun cancelOrder(orderId: String)
    suspend fun getMealTitleByMealId(mealId: Int):String


}