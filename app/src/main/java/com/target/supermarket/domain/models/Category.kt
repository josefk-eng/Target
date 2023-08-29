package com.target.supermarket.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Category(
    @PrimaryKey var id:Int = 0,
    var name:String,
    var description:String,
    var date_added:String,
    var image:String,
    var availability:Boolean,
    var ui:String
):SearchObject
