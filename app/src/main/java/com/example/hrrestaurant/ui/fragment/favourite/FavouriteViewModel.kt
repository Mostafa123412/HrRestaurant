package com.example.hrrestaurant.ui.fragment.favourite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.hrrestaurant.data.repositories.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavouriteViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    //    var favouriteItems: LiveData<List<ItemEntity>> = baseRepository.getFavouriteItems().asLiveData()
    val favouriteItems = repository.getFavouriteItems().asLiveData()
    init {

    }

}