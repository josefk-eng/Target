package com.target.supermarket.data.local.room.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.target.supermarket.domain.models.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: Category)

    @Query("SELECT * FROM category")
    fun getCategories(): Flow<List<Category>>

    @Query("SELECT * FROM category WHERE id LIKE :id")
    suspend fun getCategoryByID(id:Int): Category?

    @Query("SELECT * FROM category WHERE id LIKE :id LIMIT 1")
    fun getCategoryyByID(id:Int): Flow<Category>

    @Delete
    suspend fun delete(category: Category)
}