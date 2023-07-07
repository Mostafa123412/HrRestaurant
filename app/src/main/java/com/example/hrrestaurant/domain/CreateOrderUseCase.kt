package com.example.hrrestaurant.domain

import android.util.Log
import com.example.hrrestaurant.data.dataSources.localDataSource.Meal
import com.example.hrrestaurant.data.dataSources.localDataSource.Order
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class CreateOrderUseCase {

    suspend operator fun invoke(
        cartItems: List<Meal?>,
        currentUserId: String,
        orderLocation: String,
        userPrimaryPhone: String,
        userSecondaryPhone: String,
        userName:String
    ): Order {
        var totalPrice: Double = 0.0
        var totalOrderEstimatedTime:Double = 0.0
        val orderHashMap = HashMap<String, Int>() // Key must be string
        val orderInfoHashMap = HashMap<String, String>() // Key must be string
        cartItems.forEach { meal ->
            if (meal!!.estimatedTime.toDouble() > totalOrderEstimatedTime) totalOrderEstimatedTime =
                meal.estimatedTime.toDouble()
            totalPrice += meal.price * meal.count
            orderHashMap.put(meal.id.toString(), meal.count)
        }
        orderInfoHashMap.put("userName" , userName)
        orderInfoHashMap.put("orderLocation" , orderLocation)
        orderInfoHashMap.put("userPrimaryPhoneNum" , userPrimaryPhone)
        orderInfoHashMap.put("userSecondaryPhoneNum" , userSecondaryPhone)
        Log.d("Firebase", "Order hashMap is ${orderHashMap}")

        val orderStatus = "Delivered to Restaurant"
        return Order(
            orderRemoteId = "",
            orderInfo = orderInfoHashMap,
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