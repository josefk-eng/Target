package com.target.supermarket.domain.usecases.main

import com.target.supermarket.domain.models.Address
import com.target.supermarket.domain.repository.CheckOutRepository
import com.target.supermarket.domain.repository.MainRepo
import kotlinx.coroutines.flow.Flow

class GetAddressGlobal(private val repo:MainRepo) {
    operator fun invoke():Flow<List<Address>>{
        return repo.getAddresses()
    }
}