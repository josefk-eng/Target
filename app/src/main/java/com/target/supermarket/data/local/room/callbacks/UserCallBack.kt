package com.target.supermarket.data.local.room.callbacks

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.target.supermarket.data.local.room.daos.UserDao
import com.target.supermarket.domain.models.Customer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Provider

class UserCallBack (private val provider: Provider<UserDao>):RoomDatabase.Callback() {
    private val applicationScope = CoroutineScope(SupervisorJob())

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        applicationScope.launch(Dispatchers.IO) {
            populateDatabase()
        }
    }

    private suspend fun populateDatabase() {
//        val person = Customer(name = "Guest", status = 1)
//        provider.get().insertUSer(person)
    }

}