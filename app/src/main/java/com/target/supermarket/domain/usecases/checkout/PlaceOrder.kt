package com.target.supermarket.domain.usecases.checkout

import com.target.supermarket.domain.models.OrderDetails
import com.target.supermarket.domain.repository.CheckOutRepository

class PlaceOrder(private val repo:CheckOutRepository) {
    suspend operator fun invoke(details: OrderDetails?, result:(OrderDetails?,Exception?)->Unit){
        if (details==null){
            result(null, Exception("no order details provided"))
        }else{
            repo.placeOrder(details,result)
        }
    }
}