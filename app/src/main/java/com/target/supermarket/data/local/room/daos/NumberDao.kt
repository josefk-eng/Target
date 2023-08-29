package com.target.supermarket.data.local.room.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.target.supermarket.domain.models.PaymentNumbers
import kotlinx.coroutines.flow.Flow

@Dao
interface NumberDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNumber(number: PaymentNumbers)

    @Query("SELECT * FROM numbers")
    fun getAllNumbers(): Flow<List<PaymentNumbers>>

    @Delete
    suspend fun delete(number: PaymentNumbers)
}