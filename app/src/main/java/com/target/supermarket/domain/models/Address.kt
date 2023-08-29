package com.target.supermarket.domain.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity
data class Address(
    @PrimaryKey(autoGenerate = true) val id:Int = 0,
    val contactName:String = "",
    val district:String = "",
    val division:String = "",
    val parish:String = "",
    val village:String = "",
    val street:String = "",
    val roomNo:String = "",
    val phoneNumber:String = "",
    val email:String = "",
    val isDefault:Boolean = true,
    val autoCaptured:Boolean = true
)