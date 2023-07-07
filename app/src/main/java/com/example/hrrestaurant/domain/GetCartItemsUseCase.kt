package com.example.hrrestaurant.domain

import com.example.hrrestaurant.data.dataSources.localDataSource.Meal
import com.example.hrrestaurant.data.repositories.MealRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCartItemsUseCase @Inject constructor(private val mealRepository: MealRepository){

    operator fun invoke(): Flow<List<Meal?>> = mealRepository.getAllCartItems()

}