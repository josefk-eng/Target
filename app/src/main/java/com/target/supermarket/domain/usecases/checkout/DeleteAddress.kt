package com.target.supermarket.domain.usecases.checkout

import com.target.supermarket.domain.models.Address
import com.target.supermarket.domain.repository.CheckOutRepository

class DeleteAddress(private val repo:CheckOutRepository) {
    suspend operator fun invoke(address: Address){
        repo.deleteAddress(address)
    }
}