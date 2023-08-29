package com.target.supermarket.domain.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
    entity = Category::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("category"),
    onDelete = ForeignKey.CASCADE
), ForeignKey(
        entity = Tag::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("tag"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class Product(
    @PrimaryKey var id:Int = 0,
    var name:String,
    @ColumnInfo(defaultValue = "") var description:String,
    var price:Double,
    var date_added:String,
    var image:String,
    var category:Int,
    var availability:Boolean,
    var tag:Int,
    var unit:String,
):SearchObject

data class ProductResponse (
    var page: Int,
    var per_page: Int,
    var total: Int,
    var total_pages: Int,
    var data: List<Product>,
//    var support: Support
)

data class Support(
    var url: String,
    var text: String
)