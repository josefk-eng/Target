package com.target.supermarket.domain.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity
data class Banner(
    @PrimaryKey val id:Int,
    val name:String,
    val image:String
)
