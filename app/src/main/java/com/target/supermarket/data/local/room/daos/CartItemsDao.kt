package com.target.supermarket.data.local.room.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.target.supermarket.domain.models.CartItems
import kotlinx.coroutines.flow.Flow

@Dao
interface CartItemsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: CartItems)

    @Query("SELECT * FROM cartitems")
    fun getAllCartItems():Flow<List<CartItems>>

    @Query("SELECT * FROM cartitems WHERE id LIKE :id")
    fun getItemByProductId(id:Int): Flow<CartItems>

    @Delete
    suspend fun deleteItem(cartItem: CartItems)
}