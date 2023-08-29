package com.target.supermarket.data.local.room.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.target.supermarket.domain.models.District
import com.target.supermarket.domain.models.Division
import kotlinx.coroutines.flow.Flow

@Dao
interface DivisionDao {
    @Query("SELECT * FROM division")
    fun getAllDivisions():Flow<List<Division>>

    @Query("SELECT * FROM division WHERE district LIKE :id")
    fun getDivisionByDistrict(id:Int):Flow<List<Division>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(divisions: List<Division>)

}