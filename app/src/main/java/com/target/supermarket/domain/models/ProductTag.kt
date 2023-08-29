package com.target.supermarket.domain.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = LocalProduct::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("productId"),
            onDelete = ForeignKey.NO_ACTION
        ),
        ForeignKey(
            entity = Tag::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("tagId"),
            onDelete = ForeignKey.NO_ACTION
        ),
    ]
)
data class ProductTag(
    @PrimaryKey(autoGenerate = true) val id:Int = 0,
    val productId:Int,
    val tagId:Int
)
