package com.example.hrrestaurant.ui.fragment.ordersHistory

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.hrrestaurant.R
import com.example.hrrestaurant.databinding.FragmentOrdersHistoryBinding
import com.example.hrrestaurant.ui.adapter.OrderHistoryAdapter
import com.example.hrrestaurant.ui.base.OrderListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrdersHistoryFragment : Fragment(), OrderListener {
    private lateinit var binding: FragmentOrdersHistoryBinding
    private val orderHistoryAdapter: OrderHistoryAdapter by lazy { OrderHistoryAdapter(this@OrdersHistoryFragment) }
    private val ordersHistoryViewModel: OrdersHistoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrdersHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ordersHistoryRecyclar.adapter = orderHistoryAdapter

        ordersHistoryViewModel.allOrders.observe(viewLifecycleOwner) {
            it.let {
                orderHistoryAdapter.setNewData(it)
            }
        }
    }

    override fun orderAgain(orderId: String) {
        TODO("Not yet implemented")
    }

    override fun moreDetails(orderId: String) {
        TODO("Not yet implemented")
    }

    override fun cancelOrder(orderId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getMealTitleByMealId(mealId: Int, onTitleFound: (String) -> Unit) {
        ordersHistoryViewModel.getMealTitleByMealId(mealId)
        ordersHistoryViewModel.mealTitle.observe(viewLifecycleOwner) { mealTitle ->
            Log.d("OrderAdapter", "Meal title inside fragment is $mealTitle")
            onTitleFound(mealTitle)
        }
        Log.d("OrderAdapter", "Here after meal title inside fragment")

    }
}