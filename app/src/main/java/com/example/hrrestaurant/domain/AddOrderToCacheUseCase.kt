package com.example.hrrestaurant.domain

import com.example.hrrestaurant.data.dataSources.localDataSource.Order
import com.example.hrrestaurant.data.repositories.MealRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddOrderToCacheUseCase @Inject constructor(private val mealRepository: MealRepository) {

    operator fun invoke(order: Order) {
        CoroutineScope(Dispatchers.IO).launch {
            mealRepository.insertOrder(order)
        }
    }
}