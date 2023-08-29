package com.target.supermarket.data.local.room.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.target.supermarket.domain.models.CartItems
import com.target.supermarket.domain.models.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface LaterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: Product)

    @Query("SELECT * FROM cartitems")
    fun getAllSavedItems(): Flow<CartItems>
}