package com.example.hrrestaurant.ui.fragment.breakfast

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import com.example.hrrestaurant.R
import com.example.hrrestaurant.databinding.FragmentBreakfastBinding
import com.example.hrrestaurant.ui.adapter.HorizentalPagerAdapter
import com.example.hrrestaurant.ui.base.BaseFragment
import com.example.hrrestaurant.ui.util.AdaptersCreator
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BreakfastFragment :
    BaseFragment<FragmentBreakfastBinding>(FragmentBreakfastBinding::inflate) {
    private lateinit var breakfastPagerAdapter: HorizentalPagerAdapter
    private val breakfastViewModel: BreakfastViewModel by viewModels()
    private val tabs = listOf("general", "Croissant", "Omelette", "Pancake")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val icons =
            listOf(
                ResourcesCompat.getDrawable(resources, R.drawable.drinks, null),
                ResourcesCompat.getDrawable(resources, R.drawable.dessert, null),
                ResourcesCompat.getDrawable(resources, R.drawable.eggs_and_toast, null),
                ResourcesCompat.getDrawable(resources, R.drawable.facebook, null),
            )

        val listOfHorizentalAdapters = AdaptersCreator.createListOfHorizentalAdapters(
            tabs.size, this@BreakfastFragment,
            requireContext()
        )
        breakfastPagerAdapter = HorizentalPagerAdapter(listOfHorizentalAdapters)
        binding.viewPager.adapter = breakfastPagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = tabs[position]
            tab.icon = icons[position]
        }.attach()
        breakfastViewModel.croissant.observe(viewLifecycleOwner) {
            it.let {
                if (it != null) {
                    listOfHorizentalAdapters[1].setNewData(it)
                }
                Log.d("Repository", "Recieved ${it}")
            }
        }
        breakfastViewModel.omelette.observe(viewLifecycleOwner) {
            it.let {
                if (it != null) {
                    listOfHorizentalAdapters[2].setNewData(it)
                }
                Log.d("Repository", "Recieved ${it}")
            }
        }
        breakfastViewModel.omelette.observe(viewLifecycleOwner) {
            it.let {
                if (it != null) {
                    listOfHorizentalAdapters[2].setNewData(it)
                }
                Log.d("Repository", "Recieved ${it}")
            }
        }
        }


}

