package com.target.supermarket.domain.usecases.checkout

import com.target.supermarket.domain.models.PaymentNumbers
import com.target.supermarket.domain.repository.CheckOutRepository
import com.target.supermarket.domain.repository.MainRepo

class AddPaymentNumber(private val repo: CheckOutRepository) {
    suspend operator fun invoke(number:String, result:(Exception?)->Unit) {
        if (number.isEmpty()){
            result(Exception("Please enter your number"))
        }else if (number.length < 10){
            result(Exception("Invalid Number please check and try again"))
        }else {
            repo.addNumber(number, result)
        }
    }
}