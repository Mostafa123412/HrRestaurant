package com.example.hrrestaurant.ui.fragment.cart

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.hrrestaurant.data.dataSources.local.Meal
import com.example.hrrestaurant.data.dataSources.local.Order
import com.example.hrrestaurant.databinding.FragmentCartBinding
import com.example.hrrestaurant.ui.activity.loginActivity.LoginActivity
import com.example.hrrestaurant.ui.adapter.CartAdapter
import com.example.hrrestaurant.ui.base.BaseFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

@AndroidEntryPoint
class CartFragment : BaseFragment<FragmentCartBinding>(FragmentCartBinding::inflate) {

    private lateinit var auth: FirebaseAuth
    private val cartViewModel: CartViewModel by activityViewModels()
    private val cartAdapter: CartAdapter by lazy {
        CartAdapter(
            this@CartFragment,
            requireContext(),
            emptyList()
        )
    }
    private lateinit var currentUserId: String
    private lateinit var fireStoreDb: FirebaseFirestore

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cartListRecyclar.adapter = cartAdapter
        fireStoreDb = Firebase.firestore
        auth = FirebaseAuth.getInstance()
        if (isUserSignedIn()) currentUserId = auth.currentUser!!.uid
        cartViewModel.cartItems.observe(viewLifecycleOwner) {
            it.let {
                Log.d("Repository", "Cart Fragment Recieved ${it}")
                cartAdapter.apply {
                    setNewData(it)
                    notifyDataSetChanged()
                }
            }
        }
        binding.orderNowBtn.setOnClickListener {
            if (!isUserSignedIn()) {
                Toast.makeText(requireContext(), "Sign in to continue", Toast.LENGTH_LONG).show()
                val intent = Intent(requireContext(), LoginActivity::class.java)
                startActivity(intent)
            } else {
                if (isUserSignedIn()) currentUserId = auth.currentUser!!.uid
                val cartItems: List<Meal?>? = cartViewModel.cartItems.value
                cartItems.let {
                    Log.d("Repository", "Creating Order ....")
                    createOrder(cartItems!!)
                }
            }
        }
    }

    private fun createOrder(cartItems: List<Meal?>) {
        val orderList = HashMap<String, Int>()
        var totalPrice: Double = 0.0
        var totalOrderEstimatedTime = 0.0
        val orderHashMap = HashMap<String, Int>() // Key must be string
        cartItems.forEach { meal ->
            if (meal!!.estimatedTime > totalOrderEstimatedTime) totalOrderEstimatedTime =
                meal.estimatedTime
            totalPrice += meal.price
            orderHashMap.put(meal.id.toString(), meal.count)
        }
        Log.d("Firebase", "Order hashMap is ${orderHashMap}")

        val time = Calendar.getInstance().time
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm")
        val currentDateAndTime: String = formatter.format(time)
        val orderStatus = "Delivered to Restaurant"
        val order = hashMapOf(
            "userId" to currentUserId,
            "orderTotalPrice" to totalPrice,
            "orderDateAndTime" to currentDateAndTime,
            "orderEstimatedTime" to totalOrderEstimatedTime,
            "orderList" to orderList,
            "orderState" to orderStatus
        )
        Log.d("Firebase", "Order is ${order}")
        var orderId: String = ""
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                async {
                    fireStoreDb.collection("Orders").add(order)
                        .addOnSuccessListener { documentReference ->
                            orderId = documentReference.id
                            cartViewModel.addOrderToCache(
                                Order(
                                    orderRemoteId = orderId,
                                    userId = currentUserId,
                                    orderPrice = totalPrice,
                                    orderDateAndTime = currentDateAndTime,
                                    orderTotalEstimatedTime = totalOrderEstimatedTime,
                                    orderList = orderList,
                                    orderStatus = orderStatus
                                )
                            )
                            Log.d("Firebase", "Order id ${documentReference.id}")
                            Toast.makeText(
                                requireContext(), "Order Sent Successfully",
                                Toast.LENGTH_LONG
                            ).show()
                            getOrderById(orderId)
                        }.addOnFailureListener { exception ->
                            Toast.makeText(
                                requireContext(), "Order Failed ${exception.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                }.await()
                Log.d("Firebase", "async")
                getOrdersId()
                fireStoreDb.collection("orders").get()
                    .addOnSuccessListener { result ->
                        for (document in result) {
                            Log.d(
                                "Firebase",
                                "order id${document.id} => order data ${document.data}"
                            )
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.d("Firebase", "Error getting documents.", exception)
                    }
            }
        }
    }

    private suspend fun getOrdersId() {
        fireStoreDb.collection("Orders").document().get().addOnSuccessListener { documentSnapShot ->
            Log.d("Firebase", "document data ${documentSnapShot.data}")
            Log.d("Firebase", "document id ${documentSnapShot.id}")
            Log.d("Firebase", "document meta data ${documentSnapShot.metadata}")
        }
        val documentPath = fireStoreDb.collection("Orders").document().path
        Log.d("Firebase", "document path ${documentPath}")
        val documentParent = fireStoreDb.collection("Orders").document().parent
        Log.d("Firebase", "document parent ${documentParent}")
    }

    private fun getOrderById(orderId: String) {
        fireStoreDb.collection("Orders").document(orderId).get()
            .addOnSuccessListener { documentSnapShot ->
                Log.d("Firebase", "new order data ${documentSnapShot.data}")
                Log.d("Firebase", "new order id ${documentSnapShot.id}")
                Log.d(
                    "Firebase",
                    "new order orderList ${documentSnapShot.data?.get("orderList")}"
                )
                Log.d("Firebase", "new order meta data ${documentSnapShot.metadata}")
            }
    }

    private fun isUserSignedIn(): Boolean = auth.currentUser != null

}