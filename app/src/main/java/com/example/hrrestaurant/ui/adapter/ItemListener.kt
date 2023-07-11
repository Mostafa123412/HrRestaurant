package com.example.hrrestaurant.ui.adapter

interface ItemListener{
    fun addItemToCart(id:Int)
    fun removeItemFromCart(id:Int)
    fun incrementItemCount(id:Int)
    fun decrementItemCount(id:Int)
    fun setItemCountToZero(id: Int)
    fun addItemToFavourite(id: Int)
    fun removeItemFromFavourite(id: Int)


//    fun rateItem(id: Int?, rate: Float?): String

}