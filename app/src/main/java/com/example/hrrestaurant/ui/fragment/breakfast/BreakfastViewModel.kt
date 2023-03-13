package com.example.hrrestaurant.ui.fragment.breakfast

import androidx.lifecycle.*
import com.example.hrrestaurant.data.repositories.Repository
import com.example.hrrestaurant.ui.base.SharedViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class BreakfastViewModel @Inject constructor(
    private val baseRepository: Repository
) : SharedViewModel(baseRepository) {

    var croissant = baseRepository.getCroissant().asLiveData()
    var general = baseRepository.getGeneral().asLiveData()
    var pancake = baseRepository.getPancake().asLiveData()
    var omelette = baseRepository.getOmelette().asLiveData()

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
            }
        }
    }


}