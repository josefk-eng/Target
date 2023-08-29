package com.target.supermarket.data.local.room.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.target.supermarket.domain.models.District
import com.target.supermarket.domain.models.Division
import com.target.supermarket.domain.models.Village
import kotlinx.coroutines.flow.Flow

@Dao
interface VillageDao {
    @Query("SELECT * FROM village")
    fun getAllVillage():Flow<List<Village>>

    @Query("SELECT * FROM village WHERE parish LIKE :id")
    fun getAllVillageByParish(id:Int):Flow<List<Village>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(village: List<Village>)
}