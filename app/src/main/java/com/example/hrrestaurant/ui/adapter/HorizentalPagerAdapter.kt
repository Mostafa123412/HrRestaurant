package com.example.hrrestaurant.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hrrestaurant.databinding.HorizentalPagerItemBinding
import com.example.hrrestaurant.ui.base.PolyMorphism

class HorizentalPagerAdapter (private val recyclarViews : List<PolyMorphism>):
    RecyclerView.Adapter<HorizentalPagerAdapter.ViewPagerViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
        val view = HorizentalPagerItemBinding.inflate(LayoutInflater.from(parent.context) , parent, false)
        return ViewPagerViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        holder.binding.recyclarView.adapter = recyclarViews[position].adapter
    }

    override fun getItemCount(): Int {
        return recyclarViews.size
    }
    inner class  ViewPagerViewHolder(val binding: HorizentalPagerItemBinding): RecyclerView.ViewHolder(binding.root)

}