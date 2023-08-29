package com.target.supermarket.domain.usecases.checkout

import com.target.supermarket.domain.models.PaymentNumbers
import com.target.supermarket.domain.repository.CheckOutRepository

class UpdateNumber(private val repo:CheckOutRepository) {
    suspend operator fun invoke(number:PaymentNumbers, isDelete:Boolean=false){
        repo.updateNumber(number, isDelete)
    }
}