package com.example.hrrestaurant.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.hrrestaurant.data.dataSources.localDataSource.Meal
import com.example.hrrestaurant.databinding.HorizentalItemBinding
import com.example.hrrestaurant.ui.base.PolyMorphism

class HorizontalAdapter(
    private val listener: ItemListener,
) :
    RecyclerView.Adapter<HorizontalAdapter.ItemViewHolder>(), PolyMorphism {
    private var oldList: List<Meal?> = emptyList()

    override var adapter: RecyclerView.Adapter<*> = this


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = oldList[position]!!
//        holder.binding.favCheckBox.isChecked = item.isChecked
//        holder.binding.shoppingCart.isChecked = item.isAddedToChart

        holder.binding.apply {
            item.apply {
                favCheckBox.isChecked = isChecked
                shoppingCart.isChecked = isAddedToChart
            }

            favCheckBox.setOnCheckedChangeListener { checkBox, isChecked ->
                if (isChecked) {
                    favCheckBox.isChecked = true
                    listener.addItemToFavourite(item.id)
                } else {
                    favCheckBox.isChecked = false
                    listener.removeItemFromFavourite(item.id)
                }
            }

            shoppingCart.setOnCheckedChangeListener { checkBox, isChecked ->
                if (isChecked) {
                    shoppingCart.isChecked = true
                    listener.addItemToCart(item.id)
                    listener.incrementItemCount(item.id)
                } else {
                    shoppingCart.isChecked = false
                    listener.removeItemFromCart(item.id)
                    listener.setItemCountToZero(item.id)
                }
            }
            itemImg.load(item.itemImage)
            itemTitle.text = item.title
            description.text = item.description
            estimatedTimeValue.text = item.estimatedTime.toString().plus("Min")
            priceValue.text = item.price.toString().plus("EGP")

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