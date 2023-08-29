package com.target.supermarket.data.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.google.gson.GsonBuilder
import com.target.supermarket.utilities.Constants
import com.target.supermarket.data.local.UserPreference
import com.target.supermarket.data.local.room.TargetDB
import com.target.supermarket.data.local.room.callbacks.UserCallBack
import com.target.supermarket.data.local.room.daos.UserDao
import com.target.supermarket.data.remote.TargetApi
import com.target.supermarket.data.remote.client
import com.target.supermarket.data.repositories.CheckOutRepoImpl
import com.target.supermarket.data.repositories.HomeRepositoryImpl
import com.target.supermarket.data.repositories.MainRepoImpl
import com.target.supermarket.data.repositories.ProductsRepoImpl
import com.target.supermarket.domain.repository.CheckOutRepository
import com.target.supermarket.domain.repository.HomeRepository
import com.target.supermarket.domain.repository.MainRepo
import com.target.supermarket.domain.repository.ProductRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun providesLocalDB(
        @ApplicationContext context: Context,
        provider: Provider<UserDao>
    ): TargetDB = Room.databaseBuilder(
        context,
        TargetDB::class.java,
        "TargetDB",
    )
//        .fallbackToDestructiveMigration()
        .addCallback(UserCallBack(provider))
        .build()


    @Provides
    @Singleton
    fun provideUserDao(db: TargetDB): UserDao = db.userDao()


    @Provides
    @Singleton
    fun providesTargetApi(): TargetApi = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .client(client())
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(TargetApi::class.java)

    @Provides
    @Singleton
    fun providesPrefs(app: Application): UserPreference = UserPreference(app)


    @Provides
    @Singleton
    fun providesHomeRepository(api: TargetApi, db: TargetDB):HomeRepository = HomeRepositoryImpl(api,db)

    @Provides
    @Singleton
    fun providesProductsRepository(api: TargetApi, db: TargetDB):ProductRepository = ProductsRepoImpl(db,api)

    @Provides
    @Singleton
    fun providesMainRepo(api: TargetApi, db: TargetDB, pref:UserPreference):MainRepo = MainRepoImpl(api,db, pref)

    @Provides
    @Singleton
    fun providesCheckoutRepo(api: TargetApi, db: TargetDB):CheckOutRepository = CheckOutRepoImpl(api,db)

}