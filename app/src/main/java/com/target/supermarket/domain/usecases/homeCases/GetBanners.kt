package com.target.supermarket.domain.usecases.homeCases

import com.target.supermarket.domain.models.Banner
import com.target.supermarket.domain.repository.MainRepo
import kotlinx.coroutines.flow.Flow

class GetBanners(private val repo: MainRepo) {
    operator fun invoke():Flow<List<Banner>>{
        return repo.getBanners()
    }
}