package com.target.supermarket.data.local.room.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.target.supermarket.domain.models.Tag
import kotlinx.coroutines.flow.Flow

@Dao
interface TagDao {

    @Query("SELECT * from Tag")
    fun getAllTags():Flow<Tag>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTag(tag: Tag)

    @Query("SELECT * FROM tag WHERE id Like :id LIMIT 1")
    fun getTagById(id:Int):Flow<Tag>

    @Delete
    suspend fun deleteTag(tag: Tag)

    @Query("SELECT * FROM tag WHERE banner LIKE :id")
    fun getTagsByBannerId(id: Int): Flow<List<Tag>>
}