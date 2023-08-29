package com.target.supermarket.domain.repository

import com.target.supermarket.domain.models.Banner
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    fun getBanners():Flow<List<Banner>>
}