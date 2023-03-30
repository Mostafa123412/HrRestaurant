package com.example.hrrestaurant.ui.fragment.moreDetails

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hrrestaurant.data.dataSources.local.Meal
import com.example.hrrestaurant.domain.GetMealDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MoreDetailsViewModel @Inject constructor(private val getMealDetailsUseCase: GetMealDetailsUseCase) :
    ViewModel() {

    private var _meal = MutableLiveData<List<Meal?>>()
    val meal: LiveData<List<Meal?>>
        get() = _meal


    fun getOrderItemsByOrderId(orderId: Array<Int>) {
        val meal = viewModelScope.launch {
            withContext(Dispatchers.IO) {
                Log.d("Firebase", "order id is $orderId")
                val defferd = async { getMealDetailsUseCase(orderId) }
                val list = defferd.await()
                _meal.postValue(list)
            }
        }
    }



}