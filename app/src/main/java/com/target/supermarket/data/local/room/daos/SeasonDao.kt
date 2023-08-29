package com.target.supermarket.data.local.room.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.target.supermarket.domain.models.Season
import kotlinx.coroutines.flow.Flow

@Dao
interface SeasonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSeason(season: Season)

    @Query("SELECT * FROM season WHERE isActive LIKE 1 LIMIT 1")
    fun getActiveSeason():Flow<Season>
}