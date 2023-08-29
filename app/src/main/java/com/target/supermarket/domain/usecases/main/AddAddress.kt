package com.target.supermarket.domain.usecases.main

import com.target.supermarket.domain.models.Address
import com.target.supermarket.domain.repository.CheckOutRepository
import com.target.supermarket.domain.repository.MainRepo

class AddAddress(private val repo: CheckOutRepository) {
    suspend operator fun invoke(address: Address){
        repo.addAddress(address, isAuto = false)
    }
}
