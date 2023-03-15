package com.example.hrrestaurant.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.hrrestaurant.data.dataSources.local.Meal
import com.example.hrrestaurant.databinding.VerticalItemBinding
import com.example.hrrestaurant.ui.base.PolyMorphism

class VerticalAdapter(
    private val listener: ItemListener,
    private val context: Context,
    private var oldList: List<Meal?>
) :
    RecyclerView.Adapter<VerticalAdapter.ItemViewHolder>(), PolyMorphism {
    override var adapter: RecyclerView.Adapter<*> = this

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            VerticalItemBinding.inflate(
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
        val item = oldList[position]!!
        item.let {
            holder.binding.apply {
                item.apply {
                    if (isChecked) favCheckBox.isChecked = true
                    if (isAddedToChart) {
                        shoppingCart.isChecked = true
                        currentCount.text = item.count.toString()
                        increase.visibility = View.VISIBLE
                        decrease.visibility = View.VISIBLE
                        currentCount.visibility = View.VISIBLE
                    }
                }
                increase.setOnClickListener {
                    listener.incrementItemCount(item.id)
                    currentCount.text = item.count.toString()
                }
                decrease.setOnClickListener {
                    if (item.count == 1) {
                        listener.decrementItemCount(item.id)
                        listener.removeItemFromCart(item.id)
                        shoppingCart.isChecked = false
                        increase.visibility = View.GONE
                        decrease.visibility = View.GONE
                        currentCount.visibility = View.GONE
                        currentCount.text = "1"
                    } else {
                        listener.decrementItemCount(item.id)
                        currentCount.text = item.count.toString()
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
                shoppingCart.setOnCheckedChangeListener { checkBox, isChecked ->
                    if (isChecked) {
                        listener.addItemToCart(item.id)
                        listener.incrementItemCount(item.id)
                        Log.d("Repository", "Adding ${item.id} to Cart....")
                        currentCount.text = "1"
                        increase.visibility = View.VISIBLE
                        decrease.visibility = View.VISIBLE
                        currentCount.visibility = View.VISIBLE
                    } else {
                        increase.visibility = View.GONE
                        decrease.visibility = View.GONE
                        currentCount.visibility = View.GONE
                        currentCount.text = "1"
                        listener.setItemCountToZero(item.id)
                        listener.removeItemFromCart(item.id)
                    }
                }
                itemImg.load(item.itemImage)
                priceValue.text = item.price.toInt().toString()
                itemTitle.text = item.title
                description.text = item.description
                estimatedTimeValue.text = item.estimatedTime.toString()
            }
        }
    }

    fun setNewData(newList: List<Meal?>) {
        val diffUtil = MyDiffUtil(newList, oldList)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        oldList = newList
        diffResult.dispatchUpdatesTo(this)

    }

    inner class ItemViewHolder(val binding: VerticalItemBinding) :
        RecyclerView.ViewHolder(binding.root)


}