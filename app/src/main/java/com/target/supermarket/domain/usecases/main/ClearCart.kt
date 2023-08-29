package com.target.supermarket.domain.usecases.main

import com.target.supermarket.domain.repository.MainRepo

class ClearCart(private val repo:MainRepo) {
    suspend operator fun invoke(){
        repo.clearCart()
    }
}