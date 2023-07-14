package com.example.hrrestaurant.ui.fragment.lunch

import androidx.lifecycle.asLiveData
import com.example.hrrestaurant.data.repositories.MealRepository
import com.example.hrrestaurant.ui.base.SharedViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LunchViewModel @Inject constructor(private val baseMealRepository: MealRepository) :
    SharedViewModel(baseMealRepository) {

    var soup = baseMealRepository.getSoup().asLiveData()
    var appetizers = baseMealRepository.getAppetizers().asLiveData()
    var salade = baseMealRepository.getSalades().asLiveData()
    var burger = baseMealRepository.getBurger().asLiveData()
    var sandwiches = baseMealRepository.getSandwiches().asLiveData()
    var pasta = baseMealRepository.getPasta().asLiveData()
    var pizza = baseMealRepository.getPizza().asLiveData()
    var chicken = baseMealRepository.getChicken().asLiveData()
    var beef = baseMealRepository.getBeef().asLiveData()
    var mixDishes = baseMealRepository.getMixDishes().asLiveData()
    var steek = baseMealRepository.getRibEyeSteak().asLiveData()
    var seafood = baseMealRepository.getSeaFood().asLiveData()
    var fajita = baseMealRepository.getFajitaDishes().asLiveData()
    var sideDishes = baseMealRepository.getSideDishes().asLiveData()
    var sauces = baseMealRepository.getSauces().asLiveData()


}