package com.target.supermarket.data.local.room.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.target.supermarket.domain.models.District
import com.target.supermarket.domain.models.Division
import com.target.supermarket.domain.models.Parish
import kotlinx.coroutines.flow.Flow

@Dao
interface ParishDao {
    @Query("SELECT * FROM parish")
    fun getAllParish():Flow<List<Parish>>

    @Query("SELECT * FROM parish WHERE division LIKE :id")
    fun getAllParishByDivision(id:Int):Flow<List<Parish>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(parishes: List<Parish>)
}