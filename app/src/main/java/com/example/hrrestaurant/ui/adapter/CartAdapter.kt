package com.example.hrrestaurant.ui.adapter

import com.example.hrrestaurant.databinding.CartItemBinding
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.hrrestaurant.data.dataSources.localDataSource.Meal

class CartAdapter(
    private val listener: ItemListener,
) :
    RecyclerView.Adapter<CartAdapter.ItemViewHolder>() {

    private var oldList: List<Meal?> = emptyList()

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = oldList[position]!!
        holder.binding.apply {
            favCheckBox.isChecked = item.isChecked
            if (item.isAddedToChart) {
                currentCount.text = item.count.toString()
                shoppingCart.isChecked = true
                increase.visibility = View.VISIBLE
                currentCount.visibility = View.VISIBLE
                decrease.visibility = View.VISIBLE
            }
            itemImg.load(item.itemImage)
            itemTitle.text = item.title
            description.text = item.description
            estimatedTimeValue.text = item.estimatedTime.toString().plus("Min")
            priceValue.text = item.price.toString().plus("EGP")

            shoppingCart.setOnCheckedChangeListener { checkBox, isChecked ->
                if (isChecked) {
                } else {
                    listener.setItemCountToOne(item.id)
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
                    listener.removeItemFromCart(item.id)
                } else {
                    listener.decrementItemCount(item.id)
                }
            }
            increase.setOnClickListener {
                listener.incrementItemCount(item.id)
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