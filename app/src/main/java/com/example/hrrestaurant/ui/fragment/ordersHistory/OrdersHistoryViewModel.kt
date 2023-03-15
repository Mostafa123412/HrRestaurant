package com.example.hrrestaurant.ui.fragment.ordersHistory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.hrrestaurant.data.dataSources.local.Order
import com.example.hrrestaurant.data.repositories.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class OrdersHistoryViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    val allOrders = repository.getAllOrders().asLiveData()
    private var _mealTitle: MutableLiveData<String> = MutableLiveData()
    val mealTitle: LiveData<String>
        get() = _mealTitle

    fun getMealTitleByMealId(mealId: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _mealTitle.postValue(repository.getMealTitleByMealId(mealId))
            }
        }
    }

    fun updateOrderStatus(orderStatus:String , orderId:String){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                repository.changeOrderStatus(orderStatus , orderId)
            }
        }
    }


    }