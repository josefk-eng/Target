package com.target.supermarket.domain.usecases.homeCases

import com.target.supermarket.domain.models.LocalProduct
import com.target.supermarket.domain.repository.MainRepo
import kotlinx.coroutines.flow.Flow

class GetProducts(private val repo:MainRepo) {
    operator fun invoke():Flow<List<LocalProduct>>{
        return repo.getProducts()
    }
}