package com.example.hrrestaurant.ui.fragment.lunch

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.hrrestaurant.databinding.FragmentLunchBinding
import com.example.hrrestaurant.ui.adapter.HorizentalPagerAdapter
import com.example.hrrestaurant.ui.adapter.ItemListener
import com.example.hrrestaurant.ui.base.BaseFragment
import com.example.hrrestaurant.ui.util.AdaptersCreator
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LunchFragment : BaseFragment<FragmentLunchBinding>(FragmentLunchBinding::inflate),
    ItemListener {
    private val lunchViewModel: LunchViewModel by viewModels()
    private lateinit var pagerAdapter: HorizentalPagerAdapter
    private val tabs =
        listOf(
            "Soup",
            "Appetizers",
            "Salade",
            "Burger",
            "Sandwiches",
            "Pasta",
            "Pizza",
            "Chicken",
            "Beef",
            "Mix Dishes",
            "Rib Eye Steak",
            "Seafood",
            "Fajita dishes",
            "Side Dishes",
            "Sauces"
        )


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = requireContext()
        val listOfHorizentalAdapter = AdaptersCreator.createListOfHorizentalAdapters(tabs.size,this@LunchFragment , context)
        pagerAdapter = HorizentalPagerAdapter(
            listOfHorizentalAdapter
        )
        binding.viewPager.adapter = pagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = tabs[position]
        }.attach()



    }




}