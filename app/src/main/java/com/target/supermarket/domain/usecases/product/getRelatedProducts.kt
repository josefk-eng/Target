package com.target.supermarket.domain.usecases.product

import com.target.supermarket.domain.models.LocalProduct
import com.target.supermarket.domain.models.Product
import com.target.supermarket.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow

class getRelatedProducts(private val repo:ProductRepository) {
    operator fun invoke(item: LocalProduct): Flow<List<LocalProduct>> = repo.getRelatedProducts(item)
}