package com.example.hrrestaurant.ui.fragment.home

import androidx.lifecycle.*
import com.example.hrrestaurant.data.repositories.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val baseRepository: Repository
) : ViewModel() {

    val topRated = baseRepository.getTopRated().asLiveData()
    val mostPopular = baseRepository.getMostPopular().asLiveData()




}