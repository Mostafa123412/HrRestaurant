package com.example.hrrestaurant.ui.adapter

import com.example.hrrestaurant.ui.base.BaseListener

interface ItemListener : BaseListener {
    fun addItemToFavourite(id: Int)
    fun removeItemFromFavourite(id: Int)
    fun addItemToCart(id:Int)
    fun removeItemFromCart(id:Int)
//    fun rateItem(id: Int?, rate: Float?): String

}