package com.example.hrrestaurant.ui.fragment.moreDetails

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.hrrestaurant.data.dataSources.local.Meal
import com.example.hrrestaurant.databinding.FragmentMoreDetailsBinding
import com.example.hrrestaurant.ui.adapter.HorizontalAdapter
import com.example.hrrestaurant.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoreDetailsFragment :
    BaseFragment<FragmentMoreDetailsBinding>(FragmentMoreDetailsBinding::inflate) {

    private val args: MoreDetailsFragmentArgs by navArgs()
    private val moreDetailsFragmentViewModel: MoreDetailsViewModel by viewModels()
    private val moreDetailsHorizontalAdapter: HorizontalAdapter by lazy { HorizontalAdapter(this@MoreDetailsFragment) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.moreDetailsRecyclerView.adapter = moreDetailsHorizontalAdapter
        var counter: Int = 0
        var meals = mutableListOf<Int>()
        Log.d("Firebase", "args = ${args.orderId}")

        args.orderId.map {

            meals.add(counter, Character.getNumericValue(it))
            Log.d("Firebase", "it = $it")

            counter++
        }
        moreDetailsFragmentViewModel.getOrderItemsByOrderId(meals.toTypedArray())

        moreDetailsFragmentViewModel.meal.observe(viewLifecycleOwner) {
            Log.d("Firebase", "meal is $it")
//            meals.add(it)
            moreDetailsHorizontalAdapter.setNewData(it)
        }
    }

}