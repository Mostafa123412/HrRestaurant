package com.example.hrrestaurant.domain

import android.util.Log
import com.example.hrrestaurant.data.dataSources.local.Meal
import com.example.hrrestaurant.data.dataSources.local.Order
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class CreateOrderUseCase {

    suspend operator fun invoke(
        cartItems: List<Meal?>, currentUserId: String,
    ): Order {
        var totalPrice: Double = 0.0
        var totalOrderEstimatedTime:Double = 0.0
        val orderHashMap = HashMap<String, Int>() // Key must be string
        cartItems.forEach { meal ->
            if (meal!!.estimatedTime.toDouble() > totalOrderEstimatedTime) totalOrderEstimatedTime =
                meal.estimatedTime.toDouble()
            totalPrice += meal.price * meal.count
            orderHashMap.put(meal.id.toString(), meal.count)
        }
        Log.d("Firebase", "Order hashMap is ${orderHashMap}")

        val orderStatus = "Delivered to Restaurant"
        return Order(
            orderRemoteId = "",
            userId = currentUserId,
            orderPrice = totalPrice,
            orderDateAndTime = getCurrentDateAndTime(),
            orderTotalEstimatedTime = totalOrderEstimatedTime.toString(),
            orderList = orderHashMap,
            orderStatus = orderStatus
        )
    }

    private fun getCurrentDateAndTime(): String {
        val time = Calendar.getInstance().time
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm")
        return formatter.format(time)
    }
}