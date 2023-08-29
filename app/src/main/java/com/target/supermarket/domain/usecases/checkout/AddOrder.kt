package com.target.supermarket.domain.usecases.checkout

import com.target.supermarket.domain.models.OrderDetails
import com.target.supermarket.domain.repository.CheckOutRepository

class AddOrder(private val repo:CheckOutRepository) {
    suspend operator fun invoke(details: OrderDetails){
        repo
    }
}