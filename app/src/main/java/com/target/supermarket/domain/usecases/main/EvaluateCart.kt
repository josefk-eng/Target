package com.target.supermarket.domain.usecases.main

import com.target.supermarket.domain.models.CartItems
import com.target.supermarket.domain.repository.MainRepo
import kotlinx.coroutines.flow.*

class EvaluateCart(private val repo:MainRepo) {
    suspend operator fun invoke():Flow<List<CartItems>>{
        return repo.getProductsInCart()
    }
}