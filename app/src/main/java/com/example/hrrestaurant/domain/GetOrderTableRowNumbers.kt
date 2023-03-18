package com.example.hrrestaurant.domain

import com.example.hrrestaurant.data.repositories.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetOrderTableRowNumbers @Inject constructor(private val repository: Repository) {
    operator fun invoke(): Flow<Int?> = repository.orderTableRowNumbers()
}