package com.example.hrrestaurant.ui.fragment.favourite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.hrrestaurant.data.repositories.MealRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavouriteViewModel @Inject constructor(private val mealRepository: MealRepository) : ViewModel() {

    //    var favouriteItems: LiveData<List<ItemEntity>> = baseRepository.getFavouriteItems().asLiveData()
    val favouriteItems = mealRepository.getFavouriteItems().asLiveData()
    init {

    }

}