package com.example.hrrestaurant.domain

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.hrrestaurant.data.dataSources.local.Order
import com.example.hrrestaurant.data.repositories.Repository
import com.google.firebase.firestore.*
import java.io.IOException
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class CreateFireStoreOrderUseCase @Inject constructor(
    private val addListenerRegistrationUseCase: AddListenerRegistrationUseCase,
) {

    var registration: ListenerRegistration? = null

    /***
     *  suspendCoroutine function is used to convert the callback-based API into a coroutine-based API.
     *  This function takes a Continuation object as a parameter,
     *  which is used to resume the coroutine with the result or exception when it becomes available
     */
    operator fun invoke(
        order: Order,
        fireStoreDb: FirebaseFirestore,
    ) {
        fireStoreDb.collection("Orders")
            .add(createNewHashMapFromOrder(order))
            .addOnSuccessListener { documentReference ->
                val newOrderId = documentReference.id
                Log.d("Firebase", "FireStore order created")
                registration = addListenerRegistrationUseCase(newOrderId, fireStoreDb)
            }
            .addOnFailureListener { exception ->
                Log.d("Firebase", "FireStore order Failed to create")
            }
    }


    //Neglicting old order id.
    private fun createNewHashMapFromOrder(order: Order): kotlin.collections.HashMap<String, Serializable> =
        hashMapOf(
            "userId" to order.userId,
            "orderInfo" to order.orderInfo,
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