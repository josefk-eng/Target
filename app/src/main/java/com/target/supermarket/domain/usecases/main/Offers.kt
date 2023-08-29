package com.target.supermarket.domain.usecases.main

import com.target.supermarket.domain.models.LocalProduct
import com.target.supermarket.domain.models.Product
import com.target.supermarket.domain.repository.MainRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class Offers(private val repo: MainRepo) {
    operator fun invoke():Flow<List<LocalProduct>>{
        return repo.getProducts().map { products->
            products.filter { it.availability }
                .asSequence()
                .shuffled()
                .take(4)
                .toList()
        }
    }
}