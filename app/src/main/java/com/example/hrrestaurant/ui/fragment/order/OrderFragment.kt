package com.example.hrrestaurant.ui.fragment.order

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.hrrestaurant.R
import com.example.hrrestaurant.data.dataSources.local.Meal
import com.example.hrrestaurant.databinding.FragmentOrderBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderFragment : Fragment() {

    private val fireStoreDb: FirebaseFirestore by lazy { Firebase.firestore }
    private lateinit var binding: FragmentOrderBinding
    private val orderViewModel: OrderViewModel by viewModels()
    private val firebaseAuth: FirebaseAuth by lazy { Firebase.auth }
    private val currentUserId: String = firebaseAuth.currentUser!!.uid


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isUserInfoSavedBefore(requireContext())) {
            val userInfo = getDataFromSharedPreferences(requireContext())
            binding.userNameEt.setText(userInfo[0])
            binding.userLocationEt.setText(userInfo[1])
            binding.primaryPhoneNumberEt.setText(userInfo[2])
            binding.secondaryPhoneNumberEt.setText(userInfo[3])
        }
        binding.orderNow.setOnClickListener {
            var userName = binding.userNameEt.text.toString()
            var orderLocation = binding.userLocationEt.text.toString()
            var userPrimaryPhone = binding.primaryPhoneNumberEt.text.toString()
            var userSecondaryPhone = binding.secondaryPhoneNumberEt.text.toString()
            if (orderLocation.isEmpty()) {
                binding.userLocation.error = "Please Enter Location"
            } else if (userName.isEmpty()) {
                binding.userNameEt.error = "Please Enter User Name"
            } else if (userPrimaryPhone.isEmpty()) {
                binding.phoneNumber.error = "Please Enter Phone Number"
            } else if (userSecondaryPhone.isEmpty()) {
                binding.secondaryPhoneNumber.error = "Please Enter Secondary Phone Number"
            } else {
                saveDataToSharedPreferences(
                    userName,
                    orderLocation,
                    userPrimaryPhone,
                    userSecondaryPhone,
                    requireContext()
                )
                orderViewModel.cartItems.observe(viewLifecycleOwner){ cartItems ->
                cartItems.let {
                    Log.d("Repository", "cart Items = $cartItems ....")
                    orderViewModel.createNewOrder(
                        cartItems!!,
                        fireStoreDb,
                        currentUserId,
                        userName,
                        orderLocation,
                        userPrimaryPhone,
                        userSecondaryPhone
                    )
                    Toast.makeText(requireContext(), "Successful", Toast.LENGTH_LONG).show()
                    hideBottomNav()
                    findNavController().navigate(R.id.ordersHistoryFragment)
                }
            }
        }
    }}

    private fun isUserInfoSavedBefore(context: Context): Boolean {
        val sharedPreferences = context
            .getSharedPreferences("UserData", Context.MODE_PRIVATE)

        // Check if the user data is already saved in Shared Preferences
        return sharedPreferences.contains("userName") &&
                sharedPreferences.contains("userLocation") &&
                sharedPreferences.contains("userPrimaryPhoneNumber") &&
                sharedPreferences.contains("userSecondaryPhoneNumber")
    }

    private fun saveDataToSharedPreferences(
        userName: String,
        userLocation: String,
        userPrimaryPhoneNumber: String,
        userSecondaryPhoneNumber: String,
        context: Context,
    ) {
        val sharedPreferences = context
            .getSharedPreferences("UserData", Context.MODE_PRIVATE)

        with(sharedPreferences.edit()) {
            putString("userName", userName)
            putString("userLocation", userLocation)
            putString("userPrimaryPhoneNumber", userPrimaryPhoneNumber)
            putString("userSecondaryPhoneNumber", userSecondaryPhoneNumber)
            apply()
        }
    }

    fun getDataFromSharedPreferences(context: Context): List<String> {
        val sharedPreferences = context
            .getSharedPreferences("UserData", Context.MODE_PRIVATE)

        val userDataList = mutableListOf<String>()

        userDataList.add(sharedPreferences.getString("userName", "")!!)
        userDataList.add(sharedPreferences.getString("userLocation", "")!!)
        userDataList.add(sharedPreferences.getString("userPrimaryPhoneNumber", "")!!)
        userDataList.add(sharedPreferences.getString("userSecondaryPhoneNumber", "")!!)
        return userDataList
    }
    private fun hideBottomNav() {
        activity?.findViewById<View>(R.id.bottomNavigation)?.visibility = View.GONE
    }
}