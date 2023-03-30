package com.example.hrrestaurant.ui.fragment.ordersHistory

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.example.hrrestaurant.data.dataSources.local.Order
import com.example.hrrestaurant.data.repositories.Repository
import com.example.hrrestaurant.domain.AddOrderToCacheUseCase
import com.example.hrrestaurant.domain.CreateFireStoreOrderUseCase
import com.example.hrrestaurant.domain.GetOrderStatusUseCase
import com.example.hrrestaurant.domain.GetUserOrdersIdUseCase
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@HiltViewModel
class OrdersHistoryViewModel @Inject constructor(
    private val repository: Repository,
    private val addOrderToCacheUseCase: AddOrderToCacheUseCase,
    private val createFireStoreOrderUseCase: CreateFireStoreOrderUseCase,
    private val getListOfIdOfUsersOrdersUseCase: GetUserOrdersIdUseCase,
    private val application: Application,
) : AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext

    private var _orderStatus = MutableLiveData<String?>()
    val orderStatus: LiveData<String?>
        get() = _orderStatus



    private var _orders: MutableLiveData<List<Order>> = MutableLiveData()
    val orders: LiveData<List<Order>>
        get() = _orders

    private var _ordersId: MutableLiveData<List<String>?> = MutableLiveData()
    val ordersId: LiveData<List<String>?>
        get() = _ordersId

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _ordersId.postValue(getListOfIdOfUsersOrdersUseCase())
            }
        }
    }

    suspend fun getMealTitleByMealId(mealId: Int): String {
        return repository.getMealTitleByMealId(mealId)
    }

    fun addOrderToCache(order: Order) = addOrderToCacheUseCase(order)


    fun getUserOrders(userId: String) {
        viewModelScope.launch {
            repository.getOrdersByUserId(userId).collect {
                _orders.postValue(it)
            }
        }
    }


    fun createNewOrderFromPreviousOrder(
        order: Order,
        fireStoreDb: FirebaseFirestore,
    ) {
        viewModelScope.launch {
            createFireStoreOrderUseCase(order, fireStoreDb, context)

        }
    }


}