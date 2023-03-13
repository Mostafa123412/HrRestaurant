package com.example.hrrestaurant.ui.fragment.sweets

import com.example.hrrestaurant.data.repositories.Repository
import com.example.hrrestaurant.ui.base.SharedViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SweetsViewModel @Inject constructor (private val baseRepository: Repository): SharedViewModel(baseRepository) {


}