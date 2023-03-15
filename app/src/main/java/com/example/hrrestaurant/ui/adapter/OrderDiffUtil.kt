package com.example.hrrestaurant.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.hrrestaurant.data.dataSources.local.Order

class OrderDiffUtil(private val newList: List<Order>, private val oldList: List<Order>) :
    DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].orderRemoteId == newList[newItemPosition].orderRemoteId

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].orderPrice == newList[newItemPosition].orderPrice &&
                oldList[oldItemPosition].orderDateAndTime == newList[newItemPosition].orderDateAndTime &&
                oldList[oldItemPosition].orderStatus == newList[newItemPosition].orderStatus &&
                oldList[oldItemPosition].orderTotalEstimatedTime == newList[newItemPosition].orderTotalEstimatedTime
}
