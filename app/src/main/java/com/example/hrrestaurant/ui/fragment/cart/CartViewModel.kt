package com.example.hrrestaurant.ui.fragment.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.hrrestaurant.domain.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val getCartItemsUseCase: GetCartItemsUseCase,
) :  ViewModel() {


    val cartItems = getCartItemsUseCase().asLiveData()

}