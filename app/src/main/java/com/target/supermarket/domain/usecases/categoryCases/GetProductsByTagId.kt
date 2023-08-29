package com.target.supermarket.domain.usecases.categoryCases

import com.target.supermarket.domain.models.LocalProduct
import com.target.supermarket.domain.models.Product
import com.target.supermarket.domain.repository.MainRepo
import kotlinx.coroutines.flow.Flow

class GetProductsByTagId(private val repo: MainRepo) {
    suspend operator fun invoke(id:Int, callback:(Exception?)->Unit): Flow<List<LocalProduct>>{
        return repo.getProductsByTagId(id, callback)
    }
}