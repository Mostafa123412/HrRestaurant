package com.example.hrrestaurant.ui.fragment.sweets

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.hrrestaurant.databinding.FragmentSweetsBinding
import com.example.hrrestaurant.ui.adapter.ItemListener
import com.example.hrrestaurant.ui.adapter.VerticalPagerAdapter
import com.example.hrrestaurant.ui.base.BaseFragment
import com.example.hrrestaurant.ui.util.AdaptersCreator
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SweetsFragment : BaseFragment<FragmentSweetsBinding>(FragmentSweetsBinding::inflate),
    ItemListener {

    private lateinit var pagerAdapter: VerticalPagerAdapter
    private val sweetsViewModel: SweetsViewModel by viewModels()
    private val tabs = listOf("HotDesserts", "Waffles", "IceCream")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val listOfHorizentalAdapter = AdaptersCreator.createListOfHorizentalAdapters(tabs.size , this@SweetsFragment,requireContext())

        pagerAdapter = VerticalPagerAdapter(
            listOfHorizentalAdapter
        )
        binding.viewPager.adapter = pagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = tabs[position]
        }.attach()

    }


}