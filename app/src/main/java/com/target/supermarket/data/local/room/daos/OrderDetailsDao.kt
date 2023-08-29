package com.target.supermarket.data.local.room.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.target.supermarket.domain.models.OrderDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDetailsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: OrderDetails)

    @Query("SELECT * FROM orders")
    fun getAllOrders(): Flow<List<OrderDetails>>

    @Delete
    suspend fun delete(number: OrderDetails)
}