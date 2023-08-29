package com.target.supermarket.domain.usecases.main

import com.target.supermarket.domain.models.Season
import com.target.supermarket.domain.repository.MainRepo
import kotlinx.coroutines.flow.Flow

class GetSeason(private val repo:MainRepo) {
    suspend operator fun invoke():Flow<Season>{
        return repo.getActiveSeason()
    }
}