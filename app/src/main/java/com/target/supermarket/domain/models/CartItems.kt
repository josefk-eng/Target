package com.target.supermarket.domain.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
    entity = LocalProduct::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("id"),
    onDelete = ForeignKey.CASCADE
)]
)
data class CartItems(
    @PrimaryKey(autoGenerate = true) var uId:Int = 0,
    var id:Int,
    var price:Double,
    var name:String,
    var qty:Int = 0,
    var image:String
)