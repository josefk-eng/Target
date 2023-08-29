package com.target.supermarket.domain.usecases.product

import com.target.supermarket.domain.models.LocalProduct
import com.target.supermarket.domain.models.Product
import com.target.supermarket.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow

class GetProductUseCase(private val repo:ProductRepository) {
    operator fun invoke(id:Int):Flow<LocalProduct> = repo.getProductById(id)
}