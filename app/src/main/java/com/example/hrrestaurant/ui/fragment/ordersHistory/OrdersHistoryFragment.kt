package com.example.hrrestaurant.ui.fragment.ordersHistory

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.example.hrrestaurant.R
import com.example.hrrestaurant.data.dataSources.localDataSource.Order
import com.example.hrrestaurant.databinding.FragmentOrdersHistoryBinding
import com.example.hrrestaurant.ui.adapter.OrderHistoryAdapter
import com.example.hrrestaurant.ui.base.BaseFragment
import com.example.hrrestaurant.ui.base.OrderListener
import com.example.hrrestaurant.ui.util.NetworkStatus
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrdersHistoryFragment :
    BaseFragment<FragmentOrdersHistoryBinding>(FragmentOrdersHistoryBinding::inflate),
    OrderListener {
    private val orderHistoryAdapter: OrderHistoryAdapter
            by lazy { OrderHistoryAdapter(this@OrdersHistoryFragment, requireContext()) }
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

    private fun addListener(orderId: String) {
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
                    orderInfo = data.get("orderInfo") as HashMap<String, String>,
                    orderPrice = data.get("orderTotalPrice") as Double,
                    orderDateAndTime = data.get("orderDateAndTime").toString(),
                    orderTotalEstimatedTime = data.get("orderEstimatedTime").toString(),
                    orderList = data.get("orderHashMap") as HashMap<String, Int>,
                    orderStatus = data.get("orderState").toString()
                )
                ordersHistoryViewModel.addOrderToCache(oldOrder)
            }
        }
    }

    fun updatePoints(newPointsToAdd: Int, userId: String, firebaseDB: FirebaseFirestore) {
        firebaseDB.collection("Users").document(userId).get().addOnCompleteListener {
            if (it.isSuccessful) {
                val oldPointValue = it.result.get("points") as Int
                val newPointValue = oldPointValue + newPointsToAdd
                firebaseDB.collection("Users").
                document(userId).update("points", newPointValue).addOnSuccessListener {
                    Toast.makeText(requireContext(),"You got new points",Toast.LENGTH_LONG).show()
                }.addOnFailureListener {
                    Toast.makeText(
                        requireContext(),
                        "Failed to add new order points",
                        Toast.LENGTH_LONG
                    ).show()
                }
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
                        orderStatus = data["orderState"].toString(),
                        orderInfo = data.get("orderInfo") as HashMap<String, String>,
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

    override fun moreDetails(mealsId: List<Int>) {
        val itemsId = mealsId.toString()
        var string = ""
        Log.d("Firebase", "items id  = $itemsId")
        mealsId.forEach {
            string += "$it"
        }

        val action =
            OrdersHistoryFragmentDirections.actionOrdersHistoryFragmentToMoreDetailsFragment(string)
        findNavController().navigate(action)
    }

    override fun cancelOrder(orderId: String) {
        if (!NetworkStatus.isNetworkAvailable(requireContext())) {
            Toast.makeText(requireContext(), "No Internet Connection", Toast.LENGTH_LONG).show()
        } else {
            activity?.findViewById<View>(R.id.retrying_progressBar)?.visibility = View.VISIBLE
            fireStoreDb
                .collection("Orders")
                .document(orderId)
                .update("orderState", "Cancelled")
                .addOnSuccessListener {
                    Toast.makeText(
                        requireContext(),
                        "Order Cancelled Successfully",
                        Toast.LENGTH_LONG
                    ).show()
                    activity?.findViewById<View>(R.id.retrying_progressBar)?.visibility = View.GONE
                }.addOnFailureListener {
                    Toast.makeText(requireContext(), "Failed To Cancel Order", Toast.LENGTH_LONG)
                        .show()
                }
            activity?.findViewById<View>(R.id.retrying_progressBar)?.visibility = View.GONE

        }
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