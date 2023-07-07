package com.example.hrrestaurant.domain

import com.example.hrrestaurant.data.repositories.MealRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateOrderStatusUseCase @Inject constructor(private val mealRepository: MealRepository) {

    operator fun invoke(orderStatus: String, orderId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.IO) {
                mealRepository.changeOrderStatus(orderStatus, orderId)
            }
        }
    }
}