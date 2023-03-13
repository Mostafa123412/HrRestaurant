package com.example.hrrestaurant.ui.activity.loginActivity

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hrrestaurant.data.dataSources.remote.User
import com.example.hrrestaurant.data.repositories.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginActivityViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val _loggedInStatus = MutableLiveData<Boolean>()
    val loggedInStatus: LiveData<Boolean>
        get() = _loggedInStatus
    private var user: User = User()

    fun userLoggedIn(userId: String, userEmail: String) {
        user.apply {
            this.userId = userId
            this.userEmail = userEmail
        }
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.notifyServerByUserLogin(user)
            }
        }
    }

    fun addUserName(userName: String) {
        user.userName = userName
    }

    fun addLocation(location: String) {
        user.userLocation = location
    }

    fun addPrimaryPhoneNumber(phoneNumber: Int) {
        user.userPrimaryPhoneNumber = phoneNumber
    }

    fun addSecondaryPhoneNumber(secondaryPhoneNumber: Int) {
        user.userSecondaryPhoneNumber = secondaryPhoneNumber
    }

    fun changeLoginStatus(status: Boolean) {
        _loggedInStatus.postValue(status)
    }


}