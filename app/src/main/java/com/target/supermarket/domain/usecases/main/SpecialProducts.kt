package com.target.supermarket.domain.usecases.main

import com.target.supermarket.domain.models.Product
import com.target.supermarket.domain.repository.MainRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SpecialProducts(private val repo: MainRepo) {
    suspend operator fun invoke():Flow<List<Product>>{
        return repo.getSpecialProducts()

//        repo.getProducts().map { products->
//            products.filter { it.tag.contains("special", ignoreCase = true) }
//                .asSequence()
//                .shuffled()
//                .take(4)
//                .toList()
//        }
    }
}