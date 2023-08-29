package com.target.supermarket.domain.usecases.main

import com.target.supermarket.domain.models.OrderDetails
import com.target.supermarket.domain.repository.MainRepo
import kotlinx.coroutines.flow.Flow

class CollectOrders(private val repo:MainRepo) {
    operator fun invoke():Flow<List<OrderDetails>>{
        return repo.collectOrders()
    }
}