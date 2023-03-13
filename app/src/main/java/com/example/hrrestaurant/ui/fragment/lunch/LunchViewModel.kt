package com.example.hrrestaurant.ui.fragment.lunch

import com.example.hrrestaurant.data.repositories.Repository
import com.example.hrrestaurant.ui.base.SharedViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LunchViewModel @Inject constructor (private val baseRepository: Repository) : SharedViewModel(baseRepository) {


}