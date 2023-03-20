package com.example.hrrestaurant.ui.fragment.order

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.hrrestaurant.data.dataSources.local.Meal
import com.example.hrrestaurant.data.dataSources.local.Order
import com.example.hrrestaurant.domain.CreateFireStoreOrderUseCase
import com.example.hrrestaurant.domain.CreateOrderUseCase
import com.example.hrrestaurant.domain.GetCartItemsUseCase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val createOrderUseCase: CreateOrderUseCase,
    private val createFireStoreOrderUseCase: CreateFireStoreOrderUseCase,
    private val getCartItemsUseCase: GetCartItemsUseCase,

    ) : ViewModel() {

    private var registration: ListenerRegistration? = null
    val cartItems = getCartItemsUseCase().asLiveData()

    fun createNewOrder(
        cartItems: List<Meal?>,
        fireStoreDb: FirebaseFirestore,
        currentUserId: String,
        orderLocation: String,
        userName:String,
        userPrimaryPhone: String,
        userSecondaryPhone: String,
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val order = createOrderUseCase(
                    cartItems,
                    currentUserId,
                    orderLocation,
                    userPrimaryPhone,
                    userName,
                    userSecondaryPhone
                )
                addNewOrderToFireStore(order, fireStoreDb)
            }
        }
    }

    private fun addNewOrderToFireStore(
        order: Order,
        fireStoreDb: FirebaseFirestore,
    ) {
        fireStoreDb.disableNetwork()
        viewModelScope.launch {
            val result = createFireStoreOrderUseCase(order, fireStoreDb)
            registration = createFireStoreOrderUseCase.registration
            withContext(Dispatchers.Main) {
                Log.d("Firebase", "result is $result")
            }
        }
    }

    override fun onCleared() {
        if (registration != null) registration!!.remove()
        super.onCleared()
    }

}