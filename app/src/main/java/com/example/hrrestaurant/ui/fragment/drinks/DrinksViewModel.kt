package com.example.hrrestaurant.ui.fragment.drinks

import com.example.hrrestaurant.data.repositories.MealRepository
import com.example.hrrestaurant.ui.base.SharedViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DrinksViewModel @Inject constructor (private val baseMealRepository: MealRepository): SharedViewModel(baseMealRepository) {



}