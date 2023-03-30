package com.example.hrrestaurant.domain

import com.example.hrrestaurant.data.repositories.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetOrderStatusUseCase @Inject constructor(private val repository: Repository) {

    operator fun invoke(orderId:String): Flow<String?> = repository.getOrderStatus(orderId)
}