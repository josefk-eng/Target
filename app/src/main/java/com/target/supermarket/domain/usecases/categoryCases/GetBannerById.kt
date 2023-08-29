package com.target.supermarket.domain.usecases.categoryCases

import com.target.supermarket.domain.models.Banner
import com.target.supermarket.domain.repository.MainRepo
import kotlinx.coroutines.flow.Flow

class GetBannerById(private val repo: MainRepo) {
    operator fun invoke(id:Int):Flow<Banner>{
        return repo.getBannerById(id)
    }
}