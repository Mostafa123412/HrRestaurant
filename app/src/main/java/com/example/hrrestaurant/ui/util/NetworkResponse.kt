package com.example.hrrestaurant.ui.util

sealed class NetworkResponse<out T> {
    data class Success<T>(val data: T?) : NetworkResponse<T>()
    data class Error<T>(val errorMessage: String) : NetworkResponse<T>()
    object Loading : NetworkResponse<Nothing>()

}


sealed interface UiState {
    object Success : UiState
    object Loading : UiState
    data class Error(val errorMessage: String) : UiState
}
sealed interface OrderState {
    object DeliveredToRestaurant : OrderState
    object DeliveredToDelivery : OrderState
    object DeliveredToClient : OrderState
    object InProgress : OrderState
    data class Cancelled(val message: String) : OrderState
}
