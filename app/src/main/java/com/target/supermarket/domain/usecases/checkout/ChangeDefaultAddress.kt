package com.target.supermarket.domain.usecases.checkout

import com.target.supermarket.domain.models.Address
import com.target.supermarket.domain.repository.CheckOutRepository
import com.target.supermarket.domain.repository.MainRepo

class ChangeDefaultAddress(private val repo:CheckOutRepository) {
    suspend operator fun invoke(address: Address){
        repo.changeDefaultAddress(address)
    }
}