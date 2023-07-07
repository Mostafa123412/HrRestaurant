package com.example.hrrestaurant.domain

import com.example.hrrestaurant.data.repositories.MealRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetOrderStatusUseCase @Inject constructor(private val mealRepository: MealRepository) {

    operator fun invoke(orderId:String): Flow<String?> = mealRepository.getOrderStatus(orderId)
}