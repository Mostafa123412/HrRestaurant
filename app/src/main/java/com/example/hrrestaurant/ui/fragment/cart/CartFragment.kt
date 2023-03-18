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
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

@AndroidEntryPoint
class CartFragment : BaseFragment<FragmentCartBinding>(FragmentCartBinding::inflate) {

    private val cartViewModel: CartViewModel by activityViewModels()
    private val cartAdapter: CartAdapter by lazy { CartAdapter(this@CartFragment) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cartListRecyclar.adapter = cartAdapter

        cartViewModel.cartItems.observe(viewLifecycleOwner) {
//            if (currentUserId == null) cartAdapter.setNewData(emptyList()) else {
                it.let {
                    Log.d("Repository", "Cart Fragment Recieved ${it}")
                    cartAdapter.apply {
                        setNewData(it)
                        notifyDataSetChanged()
                    }
//                }
            }
        }
        binding.orderNowBtn.setOnClickListener {
            if (currentUserId == null) {
                Toast.makeText(requireContext(), "Sign in to continue", Toast.LENGTH_LONG).show()
                val intent = Intent(requireContext(), LoginActivity::class.java)
                startActivity(intent)
            } else {
                    val cartItems: List<Meal?>? = cartViewModel.cartItems.value
                    cartItems.let {
                        Log.d("Repository", "Creating Order ....")
                        cartViewModel.createNewOrder(cartItems!!, fireStoreDb, currentUserId!!)
                    }
            }
        }
        cartViewModel.orderTableRowNumbers.observe(viewLifecycleOwner) {
            it.let {
                Log.d("Firebase", "order table row count = $it")
            }

        }
    }

}