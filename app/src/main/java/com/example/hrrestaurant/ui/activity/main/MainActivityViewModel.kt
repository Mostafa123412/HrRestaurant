package com.example.hrrestaurant.ui.activity.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.hrrestaurant.data.repositories.Repository
import com.example.hrrestaurant.ui.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) :
    AndroidViewModel(application) {
    //    var status: LiveData<UiState> = repository.getDataFromNetworkAndCacheIt().asLiveData()
    val status: LiveData<UiState>
        get() = _status
    private val _status = MutableLiveData<UiState>()
    val isRoomEmpty = repository.isRoomEmpty().isEmpty()

    // will it cause memory leak
    private val context = getApplication<Application>().applicationContext

    init {
        isRoomEmpty
    }

    fun getDataFromInternet() {
        Log.d("Repository", "ViewModel called ............")
        viewModelScope.launch {
            repository.getDataFromNetworkAndCacheIt(context).collect {
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