package com.example.hrrestaurant.ui.fragment.drawer

import android.os.Bundle
import android.view.View
import com.example.hrrestaurant.databinding.FragmentAboutDeveloperBinding
import com.example.hrrestaurant.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AboutDeveloperFragment :
    BaseFragment<FragmentAboutDeveloperBinding>(FragmentAboutDeveloperBinding::inflate){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}