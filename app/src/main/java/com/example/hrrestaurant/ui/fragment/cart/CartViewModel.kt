package com.example.hrrestaurant.ui.fragment.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.hrrestaurant.data.dataSources.local.Order
import com.example.hrrestaurant.data.repositories.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(private val repository: Repository) :
    ViewModel() {

    val cartItems = repository.getAllCartItems().asLiveData()
    fun addOrderToCache(order:Order){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                repository.insertOrder(order)
            }
        }
    }
//
//    init {
//        cartItems
//    }
}