package com.example.hrrestaurant.domain

import com.example.hrrestaurant.data.dataSources.local.Meal
import com.example.hrrestaurant.data.repositories.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCartItemsUseCase @Inject constructor(private val repository: Repository){

    operator fun invoke(): Flow<List<Meal?>> = repository.getAllCartItems()

}