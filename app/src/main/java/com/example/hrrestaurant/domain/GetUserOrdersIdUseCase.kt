package com.example.hrrestaurant.domain

import androidx.lifecycle.LiveData
import com.example.hrrestaurant.data.repositories.Repository
import kotlinx.coroutines.flow.Flow

class GetUserOrdersIdUseCase (private val repository:Repository) {

    suspend operator fun invoke(): List<String> = repository.getUsersOrdersId()

}