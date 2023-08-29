package com.target.supermarket.data.local.room.daos

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.target.supermarket.domain.models.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: Product)

    @Query("SELECT * FROM product")
    fun getItems(): Flow<List<Product>>

    @Query("SELECT * FROM product WHERE id LIKE :id LIMIT 1")
    fun getItemById(id: Int): Flow<Product>


    @Query("SELECT * FROM product WHERE category LIKE :id")
    fun getItemsByCat(id: Int): Flow<List<Product>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllProducts(products:List<Product>)

    @Query("SELECT * FROM product WHERE tag LIKE :id")
    fun getItemsByTagId(id: Int): Flow<List<Product>>

}