package com.example.hrrestaurant.ui.fragment.search

import androidx.lifecycle.*
import com.example.hrrestaurant.data.dataSources.localDataSource.Meal
import com.example.hrrestaurant.data.repositories.MealRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.debounce
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val mealRepository: MealRepository): ViewModel() {
    
    private val _items = MutableLiveData<List<Meal?>>()
    val foundItems:LiveData<List<Meal?>>
        get() = _items
    var searchText = MutableStateFlow<String>("")

    fun getItemsBySearchText(){
        viewModelScope.launch {
            searchText.debounce(1000).collect{ searchText ->
                withContext(Dispatchers.IO){
                mealRepository.getItemBySearchText(searchText).collect{ _items.postValue(it)
                }
            }
            }
        }
    }
}