package com.example.hrrestaurant.domain

import com.example.hrrestaurant.data.dataSources.local.Order
import com.example.hrrestaurant.data.repositories.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddOrderToCacheUseCase @Inject constructor(private val repository: Repository) {

    operator fun invoke(order: Order) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.insertOrder(order)
        }
    }
}