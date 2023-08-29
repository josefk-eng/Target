package com.target.supermarket.data.local.room.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.target.supermarket.domain.models.Banner
import com.target.supermarket.domain.models.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface BannerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBanner(banner: Banner)

    @Query("SELECT * FROM banner")
    fun getAllBanners():Flow<List<Banner>>

    @Delete(entity = Banner::class)
    suspend fun deleteBanner(banner: Banner)

    @Query("SELECT * FROM banner WHERE id LIKE :id LIMIT 1")
    fun getBannerByID(id: Int):Flow<Banner>

    @Delete
    suspend fun delete(banner: Banner)
}