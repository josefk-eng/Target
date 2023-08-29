package com.target.supermarket.domain.usecases.main

import com.target.supermarket.domain.models.Category
import com.target.supermarket.domain.models.Product
import com.target.supermarket.domain.repository.MainRepo
import kotlinx.coroutines.flow.Flow

class GetCategoryById(private val repo: MainRepo) {
    suspend operator fun invoke(catId:Int): Category {
        return repo.getCategoryById(catId)
    }
}