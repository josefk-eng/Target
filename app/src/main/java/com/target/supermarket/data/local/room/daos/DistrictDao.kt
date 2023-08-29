package com.target.supermarket.data.local.room.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.target.supermarket.domain.models.District
import kotlinx.coroutines.flow.Flow

@Dao
interface DistrictDao {
    @Query("SELECT * FROM district")
    fun getAllDistricts():Flow<List<District>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(districts: List<District>)
}