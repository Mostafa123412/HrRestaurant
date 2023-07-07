package com.example.hrrestaurant.domain

import com.example.hrrestaurant.data.dataSources.localDataSource.Meal
import com.example.hrrestaurant.data.repositories.MealRepository
import javax.inject.Inject

class GetMealDetailsUseCase @Inject constructor(private val mealRepository: MealRepository) {

    suspend operator fun invoke(mealId: Array<Int>): List<Meal?> = mealRepository.getMealDetails(mealId)
}