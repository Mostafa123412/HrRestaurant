package com.example.hrrestaurant.domain

import android.util.Log
import com.example.hrrestaurant.data.dataSources.local.Order
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import java.io.IOException
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

class AddListenerRegistrationUseCase @Inject constructor(
    private val addOrderToCacheUseCase: AddOrderToCacheUseCase,
) {

    /***
     * Yes, when adding a snapshot listener to a Firestore document,
     * the Firestore client automatically retrieves the cached data if it is available and then synchronizes any changes made to the document in the remote database with the cached data.
     *
     * You don't need to handle the caching of data manually when using snapshot listeners.
    The Firestore client automatically handles caching for you to provide a seamless offline experience to your users.


     * If you want to access the cached data from the snapshot listener,
    you can simply use the snapshot parameter that is passed to the callback.
    The snapshot object contains both the cached data and the changes made to the document in the remote database.


     */
    operator fun invoke(
        newOrderId: String,
        fireStoreDb: FirebaseFirestore,
    ): ListenerRegistration {
        val docRef = fireStoreDb.collection("Orders").document(newOrderId)
        return docRef.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Log.d("Firebase", "Error getting orders: ", exception)
                return@addSnapshotListener
            } else if (snapshot != null && snapshot.exists()) {
                val data: DocumentSnapshot = snapshot
                // Do something with each order document
                val orderState = data.get("orderState").toString()
                val newOrder = Order(
                    orderRemoteId = data.id,
                    userId = data.get("userId").toString(),
                    orderInfo = data.get("orderInfo") as HashMap<String, String>,
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