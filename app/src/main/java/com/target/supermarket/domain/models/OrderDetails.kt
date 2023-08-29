package com.target.supermarket.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class OrderDetails(
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,
//    val remoteOrderId:Int = 0,
    val price:Double = 0.0,
    val status:String = "",
    val items:String = "",
    val date_added:String = "",
    val date_updated:String = "",
    val address:String = "",
    val contact:String = "",
    val identification:String = "",
    val contactName:String = "MR ..."
)
