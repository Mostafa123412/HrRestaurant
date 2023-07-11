package com.example.hrrestaurant.ui.fragment.breakfast

import androidx.lifecycle.*
import com.example.hrrestaurant.data.repositories.MealRepository
import com.example.hrrestaurant.ui.base.SharedViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class BreakfastViewModel @Inject constructor(
    private val baseMealRepository: MealRepository
) : SharedViewModel(baseMealRepository) {

    var croissant = baseMealRepository.getCroissant().asLiveData()
    var general = baseMealRepository.getGeneral().asLiveData()
    var pancake = baseMealRepository.getPancake().asLiveData()
    var omelette = baseMealRepository.getOmelette().asLiveData()




}