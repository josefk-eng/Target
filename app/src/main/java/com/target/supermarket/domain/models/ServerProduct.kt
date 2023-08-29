package com.target.supermarket.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey

data class ServerProduct(
    val category: String,
    val description: String,
    val discount: Int,
    val id: Int,
    val image: String,
    val name: String,
    val price: String,
    val quantity: Int,
    val tag: List<Int>,
    val unit:String,
    val inStock: Boolean,
    var section:String,
    var recomPoints:Int,
    var dealPoints:Int,
    var deliveryPoints:Int
)