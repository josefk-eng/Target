package com.target.supermarket.domain.usecases.main

import com.target.supermarket.domain.models.LocalProduct
import com.target.supermarket.domain.models.Product
import com.target.supermarket.domain.repository.MainRepo

class RemoveFromCart(private val repo: MainRepo) {
    suspend operator fun invoke(item: LocalProduct){
        repo.removeFromCart(item)
    }
}