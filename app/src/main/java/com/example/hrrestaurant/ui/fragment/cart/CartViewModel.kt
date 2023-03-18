package com.example.hrrestaurant.ui.fragment.cart

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.hrrestaurant.data.dataSources.local.Meal
import com.example.hrrestaurant.data.dataSources.local.Order
import com.example.hrrestaurant.data.repositories.Repository
import com.example.hrrestaurant.domain.*
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.Serializable
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val getOrderTableNuCacheUseCase: GetOrderTableRowNumbers,
    private val getCartItemsUseCase: GetCartItemsUseCase,
    private val createOrderUseCase: CreateOrderUseCase,
    private val createFireStoreOrderUseCase: CreateFireStoreOrderUseCase,
    private val application: Application,
) :
    AndroidViewModel(application) {


    private var registration: ListenerRegistration? = null
    val cartItems = getCartItemsUseCase().asLiveData()
    private val context = getApplication<Application>().applicationContext
    val orderTableRowNumbers = getOrderTableNuCacheUseCase().asLiveData()


    fun createNewOrder(
        cartItems: List<Meal?>,
        fireStoreDb: FirebaseFirestore,
        currentUserId: String,
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val order = createOrderUseCase(cartItems, currentUserId)
                addNewOrderToFireStore(order, fireStoreDb)
            }
        }
    }
    private fun addNewOrderToFireStore(
        order: Order,
        fireStoreDb: FirebaseFirestore,
    ) {
        viewModelScope.launch {
            val result = createFireStoreOrderUseCase(order, fireStoreDb)
            registration = createFireStoreOrderUseCase.registration
            makeToast(result)
        }
    }

private fun makeToast(message: String) {
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}


override fun onCleared() {
    if (registration != null) registration!!.remove()
    super.onCleared()
}
}