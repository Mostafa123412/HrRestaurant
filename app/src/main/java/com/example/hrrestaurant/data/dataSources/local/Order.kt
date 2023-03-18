package com.example.hrrestaurant.data.dataSources.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orderTable")
data class Order(
//    @PrimaryKey(autoGenerate = true)
//    val orderLocalId:Int = 0,
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo("orderRemoteId")
    val orderRemoteId: String,
//    @PrimaryKey(autoGenerate = false)
    val userId: String,
    val orderPrice: Double,
    val orderDateAndTime: String,
    val orderTotalEstimatedTime: String,
    // (<ItemId,ItemCount> , <Burger,3> , ... )
    val orderList: HashMap<String, Int>,
    var orderStatus: String,
)
