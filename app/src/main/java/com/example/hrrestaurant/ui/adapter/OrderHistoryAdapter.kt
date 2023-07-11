package com.example.hrrestaurant.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.hrrestaurant.data.dataSources.localDataSource.Order
import com.example.hrrestaurant.databinding.OrderItemBinding
import com.example.hrrestaurant.ui.base.OrderListener
import kotlinx.coroutines.*

class OrderHistoryAdapter(
    private val orderListener: OrderListener,
    private val context: Context,
) :
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
                if (isChecked) orderListener.addThisOrderItemsToCartAgain(orderKeys)
                else orderListener.removeOrderItemsFromCart(orderKeys)
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

            if (order.orderStatus == "Delivered to Client" || order.orderStatus == "Cancelled" || order.orderStatus == "Delivered To Delivery"){
                cancelOrderBtn.visibility = View.GONE
            }

            orderStatusValue.text = order.orderStatus
            orderTotalPriceValue.text = order.orderPrice.toString().plus(" P")
            orderTotalTimeValue.text = order.orderTotalEstimatedTime.toString().plus(" Min")
            orderDateAndTimeValue.text = order.orderDateAndTime


            cancelOrderBtn.setOnClickListener { orderListener.cancelOrder(order.orderRemoteId) }
            orderAgainBtn.setOnClickListener { orderListener.orderSameOrderAgain(order) }
            orderMoreDetailsBtn.setOnClickListener {
                orderListener.moreDetails(orderKeys)
                Log.d("Firebase", "order Keys = $orderKeys")
            }
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