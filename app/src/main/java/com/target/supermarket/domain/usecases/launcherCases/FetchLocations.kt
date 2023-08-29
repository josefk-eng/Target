package com.target.supermarket.domain.usecases.launcherCases

import com.target.supermarket.domain.repository.MainRepo

class FetchLocations(private val repo:MainRepo) {
    suspend operator fun invoke(callback:(Exception)->Unit){
        repo.fetchLocations(callback)
    }
}