package com.example.hrrestaurant.ui.fragment.drinks

import android.os.Bundle
import android.view.View
import com.example.hrrestaurant.databinding.FragmentDrinksBinding
import com.example.hrrestaurant.ui.adapter.VerticalPagerAdapter
import com.example.hrrestaurant.ui.base.BaseFragment
import com.example.hrrestaurant.ui.util.AdaptersCreator
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DrinksFragment : BaseFragment<FragmentDrinksBinding>(FragmentDrinksBinding::inflate){

    private lateinit var pagerAdapter: VerticalPagerAdapter

    private val tabs = listOf(
        "Hot Drinks",
        "Turkish Coffee",
        "Strong Coffee",
        "Classic Coffee",
        "Medium Coffee",
        "Milk Shakes",
        "Fresh Juices",
        "Iced Coffee",
        "Frappueeino",
        "Cocktails",
        "Smoothies",
        "Yogurt"
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val listOfVerticalAdapters = AdaptersCreator.createListOfVerticalAdapters(tabs.size,this@DrinksFragment , requireContext())
        pagerAdapter = VerticalPagerAdapter(
            listOfVerticalAdapters
        )
        binding.viewPager.adapter = pagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = tabs[position]
        }.attach()



    }


}