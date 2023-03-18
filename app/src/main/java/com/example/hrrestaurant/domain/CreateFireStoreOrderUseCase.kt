package com.example.hrrestaurant.domain

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.hrrestaurant.data.dataSources.local.Order
import com.example.hrrestaurant.data.repositories.Repository
import com.google.firebase.firestore.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class CreateFireStoreOrderUseCase @Inject constructor(
    private val addListenerRegistrationUseCase: AddListenerRegistrationUseCase,
) {

    var registration: ListenerRegistration? = null

    suspend operator fun invoke(
        order: Order,
        fireStoreDb: FirebaseFirestore,
    ): String {
        var result = ""
        fireStoreDb
            .collection("Orders")
            .add(createNewHashMapFromOrder(order))
            .addOnSuccessListener { documentReference ->
                val newOrderId = documentReference.id
                registration = addListenerRegistrationUseCase(newOrderId, fireStoreDb)
                result = "Order Sent Successfully"
            }.addOnFailureListener { exception ->
                result = "Failed${exception.message}"
            }
        return result
    }

    //Neglicting old order id.
    private fun createNewHashMapFromOrder(order: Order): kotlin.collections.HashMap<String, Serializable> =
        hashMapOf(
            "userId" to order.userId,
            "orderTotalPrice" to order.orderPrice,
            "orderDateAndTime" to getDateAndTime(),
            "orderEstimatedTime" to order.orderTotalEstimatedTime,
            "orderHashMap" to order.orderList,
            "orderState" to "Delivered to restaurant"
        )
    private fun getDateAndTime(): String {
        val time = Calendar.getInstance().time
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm")
        return formatter.format(time)
    }

}