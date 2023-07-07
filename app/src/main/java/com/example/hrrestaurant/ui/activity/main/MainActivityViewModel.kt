package com.example.hrrestaurant.ui.activity.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.hrrestaurant.data.repositories.MealRepository
import com.example.hrrestaurant.ui.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val mealRepository: MealRepository,
    application: Application
) :
    AndroidViewModel(application) {
    //    var status: LiveData<UiState> = repository.getDataFromNetworkAndCacheIt().asLiveData()
    val status: LiveData<UiState>
        get() = _status
    private val _status = MutableLiveData<UiState>()
    var isRoomEmpty: MutableLiveData<Boolean> = MutableLiveData()

    // will it cause memory leak
    private val context = getApplication<Application>().applicationContext

    init {
        viewModelScope.launch {
            async {
                mealRepository.isRoomEmpty().collect{
                    isRoomEmpty.postValue(it)
                    Log.d("Repository", "isRoom Empty = $isRoomEmpty")

                }
            }.await()
        }
    }

    fun getDataFromInternet() {
        Log.d("Repository", "Main Activity ViewModel.getDataFrom Internet called ..........")
        viewModelScope.launch {
            mealRepository.getDataFromNetworkAndCacheIt(context).collect {
                _status.postValue(it)
            }
        }
//        status = repository.getDataFromNetworkAndCacheIt().asLiveData()
    }

//    fun getDataFromInternetAndCacheIt() {
//        viewModelScope.launch {
//            withContext(Dispatchers.IO) {
//                repository.getDataFromNetworkAndCacheIt()
//            }
//        }
//    }
}