package com.example.hrrestaurant.ui.fragment.ordersHistory

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import com.example.hrrestaurant.data.dataSources.local.Order
import com.example.hrrestaurant.databinding.FragmentOrdersHistoryBinding
import com.example.hrrestaurant.ui.adapter.OrderHistoryAdapter
import com.example.hrrestaurant.ui.base.BaseFragment
import com.example.hrrestaurant.ui.base.OrderListener
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrdersHistoryFragment :
    BaseFragment<FragmentOrdersHistoryBinding>(FragmentOrdersHistoryBinding::inflate),
    OrderListener {
    private val orderHistoryAdapter: OrderHistoryAdapter by lazy { OrderHistoryAdapter(this@OrdersHistoryFragment) }
    private val ordersHistoryViewModel: OrdersHistoryViewModel by viewModels()
    private var registration: ListenerRegistration? = null
    private var idRegistration: ListenerRegistration? = null
    private var listOfIds = MutableLiveData<String>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ordersHistoryRecyclar.adapter = orderHistoryAdapter


//        ordersHistoryViewModel.ordersId.observe(viewLifecycleOwner) { newList ->
//            newList.let {
//                getOrdersByIds(newList!!)
//            }
//        }
        var idList = mutableListOf<String>()
        val ordersRef = fireStoreDb.collection("Orders")
        var difference = mutableListOf<String>()
        listOfIds.observe(viewLifecycleOwner) {
            Log.d("Firebase", "new order id received = $it")
            idList.add(it)
            difference = idList.subtract(difference).toMutableList()
            Log.d("Firebase", "differences are = $it")
//            getOrdersByIds(idList)
        }

        if (currentUserId != null) {
            // in case user signed out and another user signed in
//            orderHistoryAdapter.setNewData(emptyList())
            getOrderIdsByUserId(currentUserId!!)
            ordersHistoryViewModel.getUserOrders(currentUserId!!)
            ordersHistoryViewModel.orders.observe(viewLifecycleOwner) {
                it.let {
                    Log.d("Firebase", "this user Orders Fragment are = $it")
                    orderHistoryAdapter.setNewData(it)
                }
            }
        } else {
            orderHistoryAdapter.setNewData(emptyList())
        }

    }

    private fun getOrderIdsByUserId(userId: String) {
        val collectionRef = fireStoreDb.collection("Orders")
        idRegistration = collectionRef.whereEqualTo("userId", userId)
            .addSnapshotListener { snapShot, error ->
                if (error != null) return@addSnapshotListener
                if (snapShot != null) {
                    for (document in snapShot.documents) {
                        Log.d("Firebase", "This user Orders ids are = ${document.id}")
                        addListener(document.id)
                    }
                }
            }


    }
private fun addListener(orderId: String){
    val docRef = fireStoreDb.collection("Orders").document(orderId)
    idRegistration = docRef.addSnapshotListener { snapshot, exception ->
        if (exception != null) {
            Log.e("Firebase", "Error getting orders: ", exception)
            return@addSnapshotListener
        }
        if (snapshot != null && snapshot.exists()) {
            val data: DocumentSnapshot = snapshot
            // Do something with each order document
            val orderState = data.get("orderState").toString()
            val oldOrder = Order(
                orderRemoteId = data.id,
                userId = data.get("userId").toString(),
                orderPrice = data.get("orderTotalPrice") as Double,
                orderDateAndTime = data.get("orderDateAndTime").toString(),
                orderTotalEstimatedTime = data.get("orderEstimatedTime").toString(),
                orderList = data.get("orderHashMap") as HashMap<String, Int>,
                orderStatus = data.get("orderState").toString()
            )
            Log.d("Firebase", "order is ${data.get("orderDateAndTime")}")
            ordersHistoryViewModel.addOrderToCache(oldOrder)
        }
    }
}
    private fun getOrdersByIds(orderIds: List<String>) {
        Log.d("Firebase", "Orders id ==== $orderIds")
        if (orderIds.isEmpty()) {
        } else {
            val ordersRef = fireStoreDb.collection("Orders")

            // Create a query for the specified order IDs
            val query: Query = ordersRef.whereIn(FieldPath.documentId(), orderIds)
            // Add a snapshot listener to the query
            registration = query.addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    Log.e("Firebase", "Error getting orders: ", exception)
                    return@addSnapshotListener
                }
                for (data: DocumentSnapshot in snapshot!!.documents) {
                    // Do something with each order dataument
                    Log.d("Firebase", "${data.id} => ${data.data}")
                    var orderState = data.data?.get("orderState").toString()
                    Log.d("Firebase", "state is $orderState")
                    val newOrder = Order(
                        orderRemoteId = data.id,
                        userId = data["userId"].toString(),
                        orderPrice = data["orderTotalPrice"] as Double,
                        orderDateAndTime = data["orderDateAndTime"].toString(),
                        orderTotalEstimatedTime = data["orderEstimatedTime"].toString(),
                        orderList = data["orderHashMap"] as HashMap<String, Int>,
                        orderStatus = data["orderState"].toString()
                    )
                    ordersHistoryViewModel.addOrderToCache(newOrder)
                }
            }
        }
    }

    override fun onDestroy() {
        registration?.remove()
        idRegistration?.remove()
        super.onDestroy()
    }

    override fun orderAgain(order: Order) {
        ordersHistoryViewModel.createNewOrderFromPreviousOrder(order, fireStoreDb)
    }

    override fun moreDetails(orderId: String) {
        TODO("Not yet implemented")
    }

    override fun cancelOrder(orderId: String) {
        TODO("Not yet implemented")
    }

    override fun addItemsToCartAgain(itemsId: List<Int>) {
        itemsId.forEach {
            addItemToCart(it)
        }
    }

    override fun removeAllItemsFromCart(itemsId: List<Int>) {
        itemsId.forEach { removeItemFromCart(it) }
    }

    override suspend fun getMealTitleByMealId(mealId: Int): String {
        return ordersHistoryViewModel.getMealTitleByMealId(mealId)
    }

}