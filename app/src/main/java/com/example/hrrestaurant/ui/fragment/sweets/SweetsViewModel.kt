package com.example.hrrestaurant.ui.fragment.sweets

import androidx.lifecycle.asLiveData
import com.example.hrrestaurant.data.repositories.MealRepository
import com.example.hrrestaurant.ui.base.SharedViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SweetsViewModel @Inject constructor(private val baseMealRepository: MealRepository) :
    SharedViewModel(baseMealRepository) {

    var iceCream = baseMealRepository.getIceCream().asLiveData()
    val hotDesserts = baseMealRepository.getHotDrinks().asLiveData()
    val waffles = baseMealRepository.getwaddeles().asLiveData()

}