package com.target.supermarket.data.local.room

import android.content.Context
import androidx.annotation.NonNull
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.target.supermarket.data.local.room.daos.*
import com.target.supermarket.domain.models.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.Executors


@Database(entities = [
    Product::class,
    Category::class,
    Season::class,
    Banner::class,
    CartItems::class,
    SavedForLater::class,
    Address::class,
OrderDetails::class,
PaymentNumbers::class,
TargetNotification::class,
Customer::class,
Tag::class,
LocalProduct::class,
ProductTag::class,
District::class,
Division::class,
Parish::class,
Village::class,
Street::class,
                     ], version = 1)
abstract class TargetDB:RoomDatabase() {
    abstract fun itemDao(): ProductDao
    abstract fun categoryDao(): CategoryDao
    abstract fun seasonDao(): SeasonDao
    abstract fun bannerDao(): BannerDao
    abstract fun cartItemDao(): CartItemsDao
    abstract fun laterDao(): LaterDao
    abstract fun addressDao():AddressDao
    abstract fun numberDao():NumberDao
    abstract fun orderDao():OrderDetailsDao
    abstract fun notificationDao():NotificationDao
    abstract fun userDao():UserDao
    abstract fun tagDao():TagDao
    abstract fun localProductDao():LocalProductDao
    abstract fun productTagDao():ProductTagDao
    abstract fun districtDao():DistrictDao
    abstract fun divisionDao():DivisionDao
    abstract fun parishDao():ParishDao
    abstract fun villageDao():VillageDao
    abstract fun streetDao():StreetDao
}