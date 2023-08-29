package com.target.supermarket.domain.usecases.main

import com.target.supermarket.domain.models.LocalProduct
import com.target.supermarket.domain.models.Product
import com.target.supermarket.domain.repository.MainRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class Recommended (private val mainRepo: MainRepo){
    operator fun invoke():Flow<List<LocalProduct>>{
        return mainRepo.getProducts().map { products->
            products.filter { prod->
//                !prod.tag.contains("deal") || !prod.tag.contains("offer")
//                        || !prod.tag.contains("special")
                prod.availability
            }.asSequence()
                .take(15)
                .shuffled()
                .toList()
        }
    }
}