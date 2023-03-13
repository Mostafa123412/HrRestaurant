package com.example.hrrestaurant.ui.fragment.search

import android.util.Log
import androidx.lifecycle.*
import com.example.hrrestaurant.data.dataSources.local.Meal
import com.example.hrrestaurant.data.repositories.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.debounce
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val repository: Repository): ViewModel() {
    
    private val _items = MutableLiveData<List<Meal?>>()
    val foundItems:LiveData<List<Meal?>>
        get() = _items
    var searchText = MutableStateFlow<String>("")

    fun getItemsBySearchText(){
        viewModelScope.launch {
            searchText.debounce(1000).collect{ searchText ->
                withContext(Dispatchers.IO){
                repository.getItemBySearchText(searchText).collect{ _items.postValue(it)
                }
            }
            }
        }
    }
}