package com.target.supermarket.data.local.room.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.target.supermarket.domain.models.Address
import kotlinx.coroutines.flow.Flow

@Dao
interface AddressDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAddress(address: Address)

    @Query("SELECT * FROM address")
    fun getAllAddress(): Flow<List<Address>>

    @Query("SELECT * FROM address WHERE autoCaptured LIKE 1 LIMIT 1")
    fun getAutoAddress():Flow<Address>

    @Delete
    suspend fun deleteAddress(address: Address)
}