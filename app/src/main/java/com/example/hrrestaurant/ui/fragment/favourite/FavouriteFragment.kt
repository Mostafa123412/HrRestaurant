package com.example.hrrestaurant.ui.fragment.favourite

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import com.example.hrrestaurant.databinding.FragmentFavouriteBinding
import com.example.hrrestaurant.ui.adapter.HorizentalAdapter
import com.example.hrrestaurant.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavouriteFragment :
    BaseFragment<FragmentFavouriteBinding>(FragmentFavouriteBinding::inflate) {
    private val favouriteViewModel: FavouriteViewModel by viewModels()
    private val favouriteAdapter: HorizentalAdapter by lazy {
        HorizentalAdapter(this, requireContext(), emptyList())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.favouriteRecyclar.adapter = favouriteAdapter

        favouriteViewModel.favouriteItems.observe(viewLifecycleOwner) {
            it.let {
                favouriteAdapter.setNewData(it)
            }
        }
    }


}