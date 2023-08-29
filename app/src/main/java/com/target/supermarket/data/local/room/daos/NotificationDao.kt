package com.target.supermarket.data.local.room.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.target.supermarket.domain.models.TargetNotification
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(notifications:List<TargetNotification>)

    @Query("SELECT * FROM targetnotification")
    fun getAll():Flow<List<TargetNotification>>

    @Delete
    suspend fun delete(notification: TargetNotification)

}