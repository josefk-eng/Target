package com.target.supermarket.domain.usecases.main

import com.target.supermarket.domain.models.LocalProduct
import com.target.supermarket.domain.models.Product
import com.target.supermarket.domain.repository.MainRepo
import kotlinx.coroutines.flow.Flow

class GetItems(private val repo:MainRepo) {
    operator fun invoke():Flow<List<LocalProduct>> = repo.getProducts()
}