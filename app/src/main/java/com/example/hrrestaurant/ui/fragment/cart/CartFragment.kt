package com.example.hrrestaurant.ui.fragment.cart

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.hrrestaurant.data.dataSources.local.Meal
import com.example.hrrestaurant.data.dataSources.local.Order
import com.example.hrrestaurant.databinding.FragmentCartBinding
import com.example.hrrestaurant.ui.activity.loginActivity.LoginActivity
import com.example.hrrestaurant.ui.adapter.CartAdapter
import com.example.hrrestaurant.ui.base.BaseFragment
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cartListRecyclar.adapter = cartAdapter
        auth = FirebaseAuth.getInstance()
        if (isUserSignedIn()) currentUserId = auth.currentUser!!.uid
        cartViewModel.cartItems.observe(viewLifecycleOwner) {
            it.let {
                Log.d("Repository", "Cart Fragment Recieved ${it}")
                cartAdapter.setNewData(it)
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
        val orderList = mutableListOf<HashMap<Int, Int>>()
        var totalPrice: Double = 0.0
        var totalOrderEstimatedTime = 0.0
        cartItems.forEach { meal ->
            if (meal!!.estimatedTime > totalOrderEstimatedTime) totalOrderEstimatedTime =
                meal.estimatedTime
            val hashMap = HashMap<Int, Int>()
            hashMap[meal.id] = meal.count
            totalPrice += meal.price
            orderList.add(hashMap)
        }
        val time = Calendar.getInstance().time
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm")
        val currentDateAndTime: String = formatter.format(time)

        val order =
            Order(currentUserId, totalPrice, currentDateAndTime, totalOrderEstimatedTime, orderList)
        Log.d("Repository", "Order is $order")

    }

    private fun isUserSignedIn(): Boolean = auth.currentUser != null

}