package com.example.hrrestaurant.ui.fragment.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.hrrestaurant.data.repositories.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(private val repository: Repository) :
    ViewModel() {

    val cartItems = repository.getAllCartItems().asLiveData()
//
//    init {
//        cartItems
//    }
}