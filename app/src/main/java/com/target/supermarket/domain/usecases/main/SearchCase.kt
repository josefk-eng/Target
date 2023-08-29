package com.target.supermarket.domain.usecases.main

import com.target.supermarket.domain.models.SearchResult
import com.target.supermarket.domain.repository.MainRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchCase(private val repo:MainRepo) {
    suspend operator fun invoke(query:String):Flow<SearchResult> = flow{
        val rem = repo.searchRemote(query)
        emit(rem)
    }
}