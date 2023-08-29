package com.target.supermarket.domain.usecases.checkout

import com.target.supermarket.domain.models.Address
import com.target.supermarket.domain.repository.CheckOutRepository
import com.target.supermarket.domain.repository.MainRepo
import kotlinx.coroutines.flow.Flow

class GetAddress(private val repo:CheckOutRepository) {
    operator fun invoke():Flow<List<Address>>{
        return repo.getAddresses()
    }
}