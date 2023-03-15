package com.example.hrrestaurant.data.dataSources.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orderTable")
data class Order(
//    @PrimaryKey(autoGenerate = true)
//    val orderLocalId:Int = 0,
    @PrimaryKey(autoGenerate = false)
    val orderRemoteId:String,
    val userId:String,
    val orderPrice:Double,
    val orderDateAndTime:String,
    val orderTotalEstimatedTime:Double,
    // (<ItemId,ItemCount> , <Burger,3> , ... )
    val orderList:HashMap<String,Int>,
    var orderStatus:String,
)
