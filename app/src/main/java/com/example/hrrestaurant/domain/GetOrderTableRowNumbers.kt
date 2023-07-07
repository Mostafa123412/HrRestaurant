package com.example.hrrestaurant.domain

import com.example.hrrestaurant.data.repositories.MealRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetOrderTableRowNumbers @Inject constructor(private val mealRepository: MealRepository) {
    operator fun invoke(): Flow<Int?> = mealRepository.orderTableRowNumbers()
}