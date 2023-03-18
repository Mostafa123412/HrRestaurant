package com.example.hrrestaurant.domain

import android.util.Log
import com.example.hrrestaurant.data.dataSources.local.Order
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import javax.inject.Inject

class AddListenerRegistrationUseCase @Inject constructor(
    private val addOrderToCacheUseCase: AddOrderToCacheUseCase,
) {

    operator fun invoke(
        newOrderId: String,
        fireStoreDb: FirebaseFirestore,
    ): ListenerRegistration {
        val docRef = fireStoreDb.collection("Orders").document(newOrderId)

        return docRef.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Log.e("Firebase", "Error getting orders: ", exception)
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                val data: DocumentSnapshot = snapshot
                // Do something with each order document
                val orderState = data.get("orderState").toString()
                val newOrder = Order(
                    orderRemoteId = data.id,
                    userId = data.get("userId").toString(),
                    orderPrice = data.get("orderTotalPrice") as Double,
                    orderDateAndTime = data.get("orderDateAndTime").toString(),
                    orderTotalEstimatedTime = data.get("orderEstimatedTime").toString(),
                    orderList = data.get("orderHashMap") as HashMap<String, Int>,
                    orderStatus = data.get("orderState").toString()
                )
                Log.d("Firebase", "order is ${data.get("orderDateAndTime")}")
                addOrderToCacheUseCase(newOrder)
            }
        }
    }
}