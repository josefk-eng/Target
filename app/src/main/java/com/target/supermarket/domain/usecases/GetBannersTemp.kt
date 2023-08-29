package com.target.supermarket.domain.usecases

import com.target.supermarket.domain.models.Banner
import com.target.supermarket.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow

class GetBannersTemp(private val repo:HomeRepository) {
    suspend operator fun invoke():Flow<List<Banner>>{
        return repo.getBanners()
    }
}