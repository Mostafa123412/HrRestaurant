package com.example.hrrestaurant.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.hrrestaurant.R
import com.example.hrrestaurant.data.dataSources.local.Order
import com.example.hrrestaurant.databinding.OrderItemBinding
import com.example.hrrestaurant.ui.base.OrderListener
import kotlinx.coroutines.*

class OrderHistoryAdapter(private val orderListener: OrderListener , private val context: Context) :
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

    override fun getItemCount(): Int = oldList.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val order = oldList[position]
        val orderKeys = order.orderList.keys.map { it.toInt() }.toList()
        holder.binding.apply {
            var orderTotalTitle = ""
            addOrderItemsToCart.setOnCheckedChangeListener { checkBox, isChecked ->
                if (isChecked) orderListener.addItemsToCartAgain(orderKeys)
                else orderListener.removeAllItemsFromCart(orderKeys)
            }
            order.orderList.keys.forEach { mealId ->
                val mealCount = order.orderList[mealId]
                CoroutineScope(Dispatchers.IO).launch {
                    val oneMealTitle = async {
                        orderListener.getMealTitleByMealId(mealId.toInt())
                    }
                    withContext(Dispatchers.Main) {
                        orderTotalTitle += " $mealCount -> ${oneMealTitle.await()} ,"
                        orderTitle.text = orderTotalTitle.trim(',')
                    }
                }
            }
            if (order.orderStatus == "Delivered To Delivery"){
                val greenColor = ContextCompat.getColor(context , R.color.greenColor)
                orderStatusValue.setTextColor(greenColor)
            }
            orderStatusValue.text = order.orderStatus
            orderTotalPriceValue.text = order.orderPrice.toString().plus(" P")
            orderTotalTimeValue.text = order.orderTotalEstimatedTime.toString().plus(" Min")
            orderDateAndTimeValue.text = order.orderDateAndTime
            cancelOrderBtn.setOnClickListener { orderListener.cancelOrder(order.orderRemoteId) }
            orderAgainBtn.setOnClickListener { orderListener.orderAgain(order) }
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