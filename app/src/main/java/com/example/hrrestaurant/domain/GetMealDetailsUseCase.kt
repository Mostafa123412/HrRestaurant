package com.example.hrrestaurant.domain

import com.example.hrrestaurant.data.dataSources.local.Meal
import com.example.hrrestaurant.data.repositories.Repository
import javax.inject.Inject

class GetMealDetailsUseCase @Inject constructor(private val repository: Repository) {

    suspend operator fun invoke(mealId: Array<Int>): List<Meal?> = repository.getMealDetails(mealId)
}