package com.example.hrrestaurant.ui.fragment.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.hrrestaurant.R
import com.example.hrrestaurant.databinding.FragmentHomeBinding
import com.example.hrrestaurant.ui.adapter.HorizontalAdapter
import com.example.hrrestaurant.ui.adapter.ItemListener
import com.example.hrrestaurant.ui.adapter.VerticalAdapter
import com.example.hrrestaurant.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate), ItemListener {
    private val homeViewModel: HomeViewModel by viewModels<HomeViewModel>()

    private val offersAdapter: HorizontalAdapter by lazy {
        HorizontalAdapter(this)
    }
    private val mostPopularAdapter: VerticalAdapter by lazy {
        VerticalAdapter(this, requireContext(), emptyList())
    }
    private val topRatedAdapter: VerticalAdapter by lazy {
        VerticalAdapter(this, requireContext(), emptyList())
    }
    private lateinit var navController: NavController
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = NavHostFragment.findNavController(this)
        binding.recyclarViewOffers.adapter = offersAdapter
        binding.recyclarViewMostPopular.adapter = mostPopularAdapter
        setOnClickViews()
        homeViewModel.topRated.observe(viewLifecycleOwner) {
            it.let {
                topRatedAdapter.setNewData(it!!)
            }
        }
        homeViewModel.mostPopular.observe(viewLifecycleOwner) {
            it.let {
                mostPopularAdapter.setNewData(it!!)
            }
        }

//        findNavController()

    }

    override fun addItemToFavourite(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
//            homeViewModel.addItemToFavorite(id)
        }
    }

    override fun removeItemFromFavourite(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
//            homeViewModel.removeItemFromFavorite(id)
        }
    }

    private fun setOnClickViews() {
        binding.breakfastImg.setOnClickListener {
            hideBottomNav()
            navController.navigate(R.id.breakfastFragment)
        }
        binding.lunchImg.setOnClickListener {
            hideBottomNav()
            navController.navigate(R.id.lunchFragment)
        }
        binding.dessertImg.setOnClickListener {
            hideBottomNav()
            navController.navigate(R.id.sweetsFragment)
        }
        binding.drinksImg.setOnClickListener {
            hideBottomNav()
            navController.navigate(R.id.drinksFragment)
        }
    }

    private fun hideBottomNav() {
        activity?.findViewById<View>(R.id.bottomNavigation)?.visibility = View.GONE
    }
}