package com.target.supermarket.domain.usecases.launcherCases

import com.target.supermarket.domain.repository.MainRepo

class FetchBanners(private val repo:MainRepo) {
    suspend operator fun invoke(callback:(Exception?)->Unit){
        repo.fetchBanners(callback)
    }
}