package com.example.hrrestaurant.ui.fragment.drawer

import android.os.Bundle
import android.view.View
import com.example.hrrestaurant.databinding.FragmentAboutRestaurantBinding
import com.example.hrrestaurant.ui.adapter.SliderAdapter
import com.example.hrrestaurant.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AboutRestaurantFragment :
    BaseFragment<FragmentAboutRestaurantBinding>(FragmentAboutRestaurantBinding::inflate) {

    private lateinit var viewPagerAdapter: SliderAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // here put restaurant images
        viewPagerAdapter = SliderAdapter(listOf<Int>())
    }
}