package com.target.supermarket.domain.usecases.main

import com.target.supermarket.domain.models.Product
import com.target.supermarket.domain.repository.MainRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class Deals(private val repo: MainRepo) {
    suspend operator fun invoke():Flow<List<Product>>{
        return repo.getDeals()
//        repo.getProducts().map { products->
//            products.filter { it.tag.contains("deal", ignoreCase = true) }
//                .asSequence()
//                .shuffled()
//                .toList()
//        }
    }
}