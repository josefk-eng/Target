package com.target.supermarket.domain.usecases.checkout

import com.target.supermarket.domain.models.PaymentNumbers
import com.target.supermarket.domain.repository.CheckOutRepository
import kotlinx.coroutines.flow.Flow

class GetPayNumbers(private val repo:CheckOutRepository) {
    operator fun invoke():Flow<List<PaymentNumbers>>{
        return repo.getAllPayNumbers()
    }
}