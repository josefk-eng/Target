package com.target.supermarket.domain.usecases.main

import com.target.supermarket.domain.models.Product
import com.target.supermarket.domain.repository.MainRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetProductsByTag(private val repo:MainRepo) {
    operator fun invoke(tags:String):Flow<List<Product>>{
        return repo.getProductsByTags(tags).map { prods->
            prods
                .asSequence()
                .shuffled()
                .toList()
        }
    }
}