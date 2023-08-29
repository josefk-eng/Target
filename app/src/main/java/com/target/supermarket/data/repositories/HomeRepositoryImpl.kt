package com.target.supermarket.data.repositories

import com.target.supermarket.data.local.room.TargetDB
import com.target.supermarket.data.remote.TargetApi
import com.target.supermarket.domain.models.Banner
import com.target.supermarket.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow

class HomeRepositoryImpl(private val api: TargetApi, private val db: TargetDB):HomeRepository {
    override fun getBanners(): Flow<List<Banner>> {
        return db.bannerDao().getAllBanners()
    }
}