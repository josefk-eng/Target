package com.target.supermarket.domain.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
//    foreignKeys = [ForeignKey(
//    entity = Banner::class,
//    parentColumns = arrayOf("id"),
//    childColumns = arrayOf("banner"),
//    onDelete = ForeignKey.CASCADE
//)]
)
data class Tag(
    @PrimaryKey val id:Int,
    val name:String,
    val banner:Int,
    val isActive:Boolean
)
