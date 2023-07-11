package com.example.hrrestaurant.domain

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.hrrestaurant.data.dataSources.localDataSource.Order
import com.example.hrrestaurant.ui.util.Constants
import com.google.firebase.firestore.*
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

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
        context: Context
    ) {
        fireStoreDb.collection("Orders")
            .add(createNewHashMapFromOrder(order))
            .addOnSuccessListener { documentReference ->
                val newOrderId = documentReference.id
                registration = addListenerRegistrationUseCase(newOrderId, fireStoreDb)
                Toast.makeText(context,"Order Created Successfully", Toast.LENGTH_SHORT).show()

            }
            .addOnFailureListener { exception ->
                Toast.makeText(context, "order Failed to create", Toast.LENGTH_SHORT).show()
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
            "orderState" to "Delivered to restaurant",
            "token" to Constants.TOKEN
        )

    private fun getDateAndTime(): String {
        val time = Calendar.getInstance().time
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm")
        return formatter.format(time)
    }
}