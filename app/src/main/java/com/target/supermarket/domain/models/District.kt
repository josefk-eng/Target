package com.target.supermarket.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey

data class AddResponse(
    val id: Int,
    val name: String,
    val isActive: Boolean,
    val key:Int
)


@Entity
data class District(
    @PrimaryKey val id:Int  ,
    val name:String,
    val isActive:Boolean
)

@Entity
data class Division(
    @PrimaryKey val id: Int,
    val name:String,
    val isActive:Boolean,
    val district:Int = 0
)

@Entity
data class Parish(
    @PrimaryKey val id: Int,
    val name:String,
    val isActive:Boolean,
    val division:Int = 0
)

@Entity
data class Village(
    @PrimaryKey val id: Int,
    val name:String,
    val isActive:Boolean,
    val parish:Int = 0
)

@Entity
data class Street(
    @PrimaryKey val id: Int,
    val name:String,
    val isActive:Boolean,
    val village:Int = 0
)
