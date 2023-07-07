package com.example.hrrestaurant.ui.fragment.home

import androidx.lifecycle.*
import com.example.hrrestaurant.data.repositories.MealRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val baseMealRepository: MealRepository
) : ViewModel() {

    val topRated = baseMealRepository.getTopRated().asLiveData()
    val mostPopular = baseMealRepository.getMostPopular().asLiveData()




}