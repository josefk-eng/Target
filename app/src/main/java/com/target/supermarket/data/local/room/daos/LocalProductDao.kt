package com.target.supermarket.data.local.room.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.target.supermarket.domain.models.LocalProduct
import com.target.supermarket.domain.models.ServerProduct
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalProductDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(localProduct: LocalProduct)

    @Query("SELECT * FROM localproduct WHERE id IN (:ids)")
    fun getProductsByIds(ids:List<Int>):Flow<List<LocalProduct>>


    @Query("SELECT * FROM localproduct WHERE id LIKE :ids")
    fun getProductById(ids:Int):Flow<LocalProduct>

    @Query("SELECT * FROM localproduct")
    fun getAllProducts():Flow<List<LocalProduct>>
}