package com.example.hrrestaurant.data.dataSources.local

data class Order(
    val userId:String,
    val orderPrice:Double,
    val orderDateAndTime:String,
    val orderTotalEstimatedTime:Double,
    // (<ItemId,ItemCount> , <Burger,3> , ... )
    val orderList:List<HashMap<Int,Int>>,
)
