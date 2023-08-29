package com.target.supermarket.domain.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = [ForeignKey(entity = Product::class,parentColumns = arrayOf("id"), childColumns = arrayOf("id"), onDelete = ForeignKey.CASCADE)])
data class SavedForLater(
    @PrimaryKey var id:Int,
)
