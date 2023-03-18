package com.example.hrrestaurant.domain

import android.util.Log
import com.example.hrrestaurant.data.repositories.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateOrderStatusUseCase @Inject constructor(private val repository: Repository) {

    operator fun invoke(orderStatus: String, orderId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.IO) {
                repository.changeOrderStatus(orderStatus, orderId)
            }
        }
    }
}