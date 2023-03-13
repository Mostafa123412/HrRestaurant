package com.example.hrrestaurant.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hrrestaurant.databinding.VerticalViewPagerItemBinding
import com.example.hrrestaurant.ui.base.PolyMorphism

class VerticalPagerAdapter (private val recyclarViews : List<PolyMorphism>):
    RecyclerView.Adapter<VerticalPagerAdapter.ViewPagerViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
        val view = VerticalViewPagerItemBinding.inflate(LayoutInflater.from(parent.context) , parent, false)
        return ViewPagerViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        holder.binding.recyclarView.adapter = recyclarViews[position].adapter
    }

    override fun getItemCount(): Int {
        return recyclarViews.size
    }
    inner class  ViewPagerViewHolder(val binding: VerticalViewPagerItemBinding): RecyclerView.ViewHolder(binding.root)

}