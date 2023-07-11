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
        val listOfHorizentalAdapter = AdaptersCreator.createListOfHorizentalAdapters(
            tabs.size,
            this@LunchFragment
        )
        pagerAdapter = HorizentalPagerAdapter(
            listOfHorizentalAdapter
        )

        binding.viewPager.adapter = pagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = tabs[position]
        }.attach()

        lunchViewModel.apply {
            soup.observe(viewLifecycleOwner) {
                it?.let {
                    listOfHorizentalAdapter[0].setNewData(it)
                }
            }
            appetizers.observe(viewLifecycleOwner) {
                it?.let {
                    listOfHorizentalAdapter[1].setNewData(
                        it
                    )
                }
            }
            salade.observe(viewLifecycleOwner) { it?.let { listOfHorizentalAdapter[2].setNewData(it) } }
            burger.observe(viewLifecycleOwner) { it?.let { listOfHorizentalAdapter[3].setNewData(it) } }
            sandwiches.observe(viewLifecycleOwner) {
                it?.let {
                    listOfHorizentalAdapter[4].setNewData(
                        it
                    )
                }
            }
            pasta.observe(viewLifecycleOwner) { it?.let { listOfHorizentalAdapter[5].setNewData(it) } }
            pizza.observe(viewLifecycleOwner) { it?.let { listOfHorizentalAdapter[6].setNewData(it) } }
            chicken.observe(viewLifecycleOwner) { it?.let { listOfHorizentalAdapter[7].setNewData(it) } }
            beef.observe(viewLifecycleOwner) { it?.let { listOfHorizentalAdapter[8].setNewData(it) } }
            mixDishes.observe(viewLifecycleOwner) {
                it?.let {
                    listOfHorizentalAdapter[9].setNewData(
                        it
                    )
                }
            }
            steek.observe(viewLifecycleOwner) { it?.let { listOfHorizentalAdapter[10].setNewData(it) } }
            seafood.observe(viewLifecycleOwner) {
                it?.let {
                    listOfHorizentalAdapter[11].setNewData(
                        it
                    )
                }
            }
            fajita.observe(viewLifecycleOwner) { it?.let { listOfHorizentalAdapter[12].setNewData(it) } }
            sideDishes.observe(viewLifecycleOwner) {
                it?.let {
                    listOfHorizentalAdapter[13].setNewData(
                        it
                    )
                }
            }
            sauces.observe(viewLifecycleOwner) { it?.let { listOfHorizentalAdapter[14].setNewData(it) } }

        }


    }


}