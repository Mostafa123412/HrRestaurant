package com.example.hrrestaurant.ui.util

import android.content.Context
import com.example.hrrestaurant.ui.adapter.HorizontalAdapter
import com.example.hrrestaurant.ui.adapter.ItemListener
import com.example.hrrestaurant.ui.adapter.VerticalAdapter

class AdaptersCreator {

    companion object {
        fun createListOfHorizentalAdapters(
            numberOfAdapters: Int,
            listener: ItemListener,
        ): List<HorizontalAdapter> {
            val listOfAdapters = mutableListOf<HorizontalAdapter>()
            for (i in 0 until numberOfAdapters) {
                listOfAdapters.add(HorizontalAdapter(listener))
            }
            return listOfAdapters.toList()
        }

        fun createListOfVerticalAdapters(
            numberOfAdapters: Int,
            listener: ItemListener,
            context: Context
        ): List<VerticalAdapter> {
            val listOfAdapters = mutableListOf<VerticalAdapter>()
            for (i in 0 until numberOfAdapters) {
                listOfAdapters.add(VerticalAdapter(listener, context, emptyList()))
            }
            return listOfAdapters.toList()
        }

    }

}