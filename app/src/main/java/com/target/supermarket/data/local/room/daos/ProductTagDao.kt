package com.target.supermarket.data.local.room.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.target.supermarket.domain.models.ProductTag
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductTagDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(productTag: ProductTag)

    @Query("SELECT * FROM producttag WHERE tagId LIKE :id")
    fun getTagWithProduct(id:Int):Flow<List<ProductTag>>
}