package com.example.hrrestaurant.ui.fragment.lunch

import androidx.lifecycle.asLiveData
import com.example.hrrestaurant.data.repositories.MealRepository
import com.example.hrrestaurant.ui.base.SharedViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LunchViewModel @Inject constructor (private val baseMealRepository: MealRepository) : SharedViewModel(baseMealRepository) {

val soup = baseMealRepository.getSoup().asLiveData()
val appetizers = baseMealRepository.getAppetizers().asLiveData()
    val salade = baseMealRepository.getSalades().asLiveData()
    val burger = baseMealRepository.getBurger().asLiveData()
    val sandwiches = baseMealRepository.getSandwiches().asLiveData()
    val pasta = baseMealRepository.getPasta().asLiveData()
    val pizza = baseMealRepository.getPizza().asLiveData()
    val chicken = baseMealRepository.getChicken().asLiveData()
    val beef = baseMealRepository.getBeef().asLiveData()
    val mixDishes = baseMealRepository.getMixDishes().asLiveData()
    val steek = baseMealRepository.getRibEyeSteak().asLiveData()
    val seafood = baseMealRepository.getSeaFood().asLiveData()
    val fajita = baseMealRepository.getFajitaDishes().asLiveData()
    val sideDishes = baseMealRepository.getSideDishes().asLiveData()
    val sauces = baseMealRepository.getSauces().asLiveData()


}