package com.example.hrrestaurant.ui.fragment.cart

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.hrrestaurant.R
import com.example.hrrestaurant.databinding.FragmentCartBinding
import com.example.hrrestaurant.ui.activity.loginActivity.LoginActivity
import com.example.hrrestaurant.ui.adapter.CartAdapter
import com.example.hrrestaurant.ui.base.BaseFragment
import com.example.hrrestaurant.ui.util.NetworkStatus
import dagger.hilt.android.AndroidEntryPoint

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
            } else if (!NetworkStatus.isNetworkAvailable(requireContext())) {
                Toast.makeText(requireContext(), "Error, No Internet Connection", Toast.LENGTH_LONG)
                    .show()
                Log.d("Repository", "status ${NetworkStatus.isNetworkAvailable(requireContext())}")

            } else if (cartViewModel.cartItems.value.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Error, Empty Cart", Toast.LENGTH_LONG).show()
            } else {
                    findNavController().navigate(R.id.orderFragment)
                }
            }
    }

}