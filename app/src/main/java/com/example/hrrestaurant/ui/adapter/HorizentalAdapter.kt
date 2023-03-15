package com.example.hrrestaurant.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.hrrestaurant.data.dataSources.local.Meal
import com.example.hrrestaurant.databinding.HorizentalItemBinding
import com.example.hrrestaurant.ui.base.PolyMorphism

class HorizentalAdapter(
    private val listener: ItemListener,
    private val context: Context,
    var oldList: List<Meal?>
) :
    RecyclerView.Adapter<HorizentalAdapter.ItemViewHolder>(), PolyMorphism {

    override var adapter: RecyclerView.Adapter<*> = this


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = oldList[position]!!
        holder.binding.favCheckBox.isChecked = item.isChecked
        holder.binding.shoppingCart.isChecked = item.isAddedToChart

        holder.binding.apply {
            favCheckBox.setOnCheckedChangeListener { checkBox, isChecked ->
                if (isChecked) {
                    listener.addItemToFavourite(item.id)
                } else {
                    listener.removeItemFromFavourite(item.id)
                }
            }
            shoppingCart.setOnCheckedChangeListener { checkBox, isChecked ->
                if (isChecked) {
                    listener.addItemToCart(item.id)
                    item.count = 1
                } else {
                    listener.removeItemFromCart(item.id)
                }
            }
            mealImg.load(item.itemImage)
            mealTitle.text = item.title
            description.text = item.description
            estimatedTimeValue.text = item.estimatedTime.toString()
            priceValue.text = item.price.toInt().toString()
            ratingBar.rating = item.rate!!

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
            HorizentalItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return oldList.size
    }

    inner class ItemViewHolder(val binding: HorizentalItemBinding) :
        RecyclerView.ViewHolder(binding.root)

}