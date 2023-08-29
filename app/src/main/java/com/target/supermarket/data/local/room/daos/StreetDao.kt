package com.target.supermarket.data.local.room.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.target.supermarket.domain.models.District
import com.target.supermarket.domain.models.Division
import com.target.supermarket.domain.models.Street
import kotlinx.coroutines.flow.Flow

@Dao
interface StreetDao {
    @Query("SELECT * FROM street")
    fun getAllStreets():Flow<List<Street>>

    @Query("SELECT * FROM street WHERE village LIKE :id")
    fun getAllStreetByVillage(id:Int):Flow<List<Street>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(streets: List<Street>)
}