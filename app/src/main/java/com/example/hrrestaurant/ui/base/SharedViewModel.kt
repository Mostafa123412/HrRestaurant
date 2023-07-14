package com.example.hrrestaurant.ui.base

import androidx.lifecycle.*
import com.example.hrrestaurant.data.repositories.MealRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
open class SharedViewModel @Inject constructor(private val mealRepository: MealRepository) :
    ViewModel() {
//    private var _isAddedToCart:MutableLiveData<Boolean> = MutableLiveData(false)
//    val isAddedToCart:LiveData<Boolean>
//        get() = isAddedToCart

    private val _result = MutableLiveData<String>()
    val result: LiveData<String>
        get() = _result

    fun addItemToFavorite(id: Int) {
        viewModelScope.launch {
            mealRepository.addItemToFavourite(id)
        }
    }

    fun removeItemFromFavorite(id: Int) {
        viewModelScope.launch {
            mealRepository.removeItemFromFavourite(id)
        }
    }

    /***
     * withContext(Dispatchers.Default) makes the suspend function do something on
     * a background thread and resumes the calling thread (usually the main thread)
     * when the result is ready
     */
//    fun rateItem(id: Int?, rate: Float?) {
//        //aync , await
//        viewModelScope.launch {
//            val result = withContext(Dispatchers.Default) {
//                repository.postItemRate(id, rate)
//            }
//            _result.postValue(result)
//        }
//    }

    fun addItemToCart(id: Int) = viewModelScope.launch {
        mealRepository.addItemToCart(id)
        // does we need to change to MAIN thread to access this ?
//        _isAddedToCart.postValue(true)
    }

    fun removeItemFromCart(id: Int) = viewModelScope.launch {
        mealRepository.removeItemFromCart(id)
//        _isAddedToCart.postValue(false)
    }

    fun incrementItemCount(id: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                mealRepository.incrementItemCount(id)
            }
        }
    }

    fun decrementItemCount(id: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                mealRepository.decrementItemCount(id)
            }
        }
    }

    fun setItemCountToOne(id: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                mealRepository.setItemCountToOne(id)
            }
        }
    }

}