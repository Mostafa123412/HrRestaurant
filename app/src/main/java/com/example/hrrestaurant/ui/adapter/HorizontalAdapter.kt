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
                } else {
                    listener.removeItemFromCart(item.id)
                    listener.setItemCountToOne(item.id)
                }
            }
            itemImg.load(item.itemImage)
            itemTitle.text = item.title
            description.text = item.description
            estimatedTimeValue.text = item.estimatedTime.toString().plus(" Min")
            priceValue.text = item.price.toString().plus(" EGP")

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