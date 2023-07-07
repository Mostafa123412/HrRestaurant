package com.example.hrrestaurant.domain

import com.example.hrrestaurant.data.repositories.MealRepository

class GetUserOrdersIdUseCase (private val mealRepository:MealRepository) {

    suspend operator fun invoke(): List<String> = mealRepository.getUsersOrdersId()

}