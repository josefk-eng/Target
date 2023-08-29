package com.target.supermarket.domain.usecases.main

import com.target.supermarket.domain.models.Category
import com.target.supermarket.domain.repository.MainRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetCategories(private val repo:MainRepo) {
    suspend operator fun invoke(onError:(Exception)->Unit):Flow<List<Category>>{
        return repo.getCategories(onError).map { cats->
            cats
                .filter { it.availability }
        }
    }
}