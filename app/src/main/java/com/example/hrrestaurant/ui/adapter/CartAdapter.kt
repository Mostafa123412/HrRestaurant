package com.example.hrrestaurant.ui.adapter

import com.example.hrrestaurant.databinding.CartItemBinding
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.hrrestaurant.data.dataSources.local.Meal

class CartAdapter(
    private val listener: ItemListener,
    private val context: Context,
    private var oldList: List<Meal?>
) :
    RecyclerView.Adapter<CartAdapter.ItemViewHolder>() {

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = oldList[position]!!
        holder.binding.apply {
            if (item.isChecked) favCheckBox.isChecked = true
            if (item.isAddedToChart) {
                currentCount.text = item.count.toString()
                shoppingCart.isChecked = true
                increase.visibility = View.VISIBLE
                currentCount.visibility = View.VISIBLE
                decrease.visibility = View.VISIBLE
            }
            itemTitle.text = item.title
            description.text = item.description
            estimatedTimeValue.text = item.estimatedTime.toString()
            priceValue.text = item.price.toString()
//            ratingBar.rating = item.rate!!

            shoppingCart.setOnCheckedChangeListener { checkBox, isChecked ->
                if (isChecked) {
                } else {
                    listener.setItemCountToZero(item.id)
                    listener.removeItemFromCart(item.id)
                }
            }
            favCheckBox.setOnCheckedChangeListener { checkBox, isChecked ->
                if (isChecked) {
                    Log.d("Repository", "Adding ${item.id} to favourite....")
                    listener.addItemToFavourite(item.id)
                } else {
                    Log.d("Repository", "Removing ${item.id} to favourite....")
                    listener.removeItemFromFavourite(item.id)
                }
            }
            decrease.setOnClickListener {
                if (item.count == 1) {
                    listener.decrementItemCount(item.id)
                    currentCount.text = item.count.toString()
                    listener.removeItemFromCart(item.id)
                } else {
                    listener.decrementItemCount(item.id)
                    currentCount.text = item.count.toString()
                }
            }
            increase.setOnClickListener {
                listener.incrementItemCount(item.id)
                notifyDataSetChanged()
                currentCount.text = item.count.toString()

            }
        }
    }

    fun setNewData(newList: List<Meal?>) {
        val diffUtil = MyDiffUtil(newList, oldList)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        oldList = newList
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            CartItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return oldList.size
    }

    inner class ItemViewHolder(val binding: CartItemBinding) :
        RecyclerView.ViewHolder(binding.root)

}