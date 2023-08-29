package com.target.supermarket.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Season(
    @PrimaryKey val id:Int=0,
    val name:String,
    val isActive:Boolean
)