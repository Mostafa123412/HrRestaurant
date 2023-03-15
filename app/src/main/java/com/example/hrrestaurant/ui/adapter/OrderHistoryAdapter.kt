package com.example.hrrestaurant.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.hrrestaurant.data.dataSources.local.Order
import com.example.hrrestaurant.databinding.OrderItemBinding
import com.example.hrrestaurant.ui.base.OrderListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OrderHistoryAdapter(private val orderListener: OrderListener) :
    RecyclerView.Adapter<OrderHistoryAdapter.ItemViewHolder>() {

    private var oldList = emptyList<Order>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            OrderItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return oldList.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val order = oldList[position]
        holder.binding.apply {
            var orderTotalTitle = ""
            var oneMealTitle = ""
            order.orderList.keys.forEach { mealId ->
                val mealCount = order.orderList.get(mealId)
                CoroutineScope(Dispatchers.Default).launch {
                    orderListener.getMealTitleByMealId(mealId.toInt()) {
                        oneMealTitle = it
                        Log.d("OrderAdapter", "Meal title inside listener is $oneMealTitle")
                    }
                    Log.d("OrderAdapter", "Meal title after listener is $oneMealTitle")
                    orderTotalTitle += " $mealCount -> $oneMealTitle ,"
                    orderTitle.text = orderTotalTitle.trim(',')
                }
            }
            orderStatusValue.text = order.orderStatus
            orderTotalPriceValue.text = order.orderPrice.toString().plus(" P")
            orderTotalTimeValue.text = order.orderTotalEstimatedTime.toString().plus(" Min")
            orderDateAndTimeTv.text = order.orderDateAndTime
            cancelOrderBtn.setOnClickListener { orderListener.cancelOrder(order.orderRemoteId) }
            orderAgainBtn.setOnClickListener { orderListener.orderAgain(order.orderRemoteId) }
            orderMoreDetailsBtn.setOnClickListener { orderListener.moreDetails(order.orderRemoteId) }
        }
    }

    fun setNewData(newList: List<Order>) {
        val diffUtil = OrderDiffUtil(newList, oldList)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        oldList = newList
        diffResult.dispatchUpdatesTo(this)
    }

    inner class ItemViewHolder(val binding: OrderItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}