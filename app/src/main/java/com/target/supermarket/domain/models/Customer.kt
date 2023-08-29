package com.target.supermarket.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class Customer(
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0,
    var token:String = "",
    var deviceId:String = "",
    var isActive:Boolean = true,
    var address:String = "",
    var name:String = "",
    var email:String = "",
    var password:String = "",
    var phoneNumber:String = "",
)
