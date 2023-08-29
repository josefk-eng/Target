package com.target.supermarket.domain.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "numbers")
data class PaymentNumbers(
    @PrimaryKey(autoGenerate = false)
    val number:String,
    val transCount:Int = 0,
    val isActive:Boolean = true,
    val highestAmount:Double = 0.0
)
