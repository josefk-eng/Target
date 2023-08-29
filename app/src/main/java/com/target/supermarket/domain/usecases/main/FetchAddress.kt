package com.target.supermarket.domain.usecases.main

import com.target.supermarket.domain.models.AddResponse
import com.target.supermarket.domain.models.AddressSpec
import com.target.supermarket.domain.repository.MainRepo

class FetchAddress(private val repo: MainRepo) {
    suspend operator fun invoke(spec: AddressSpec, result:(Exception?, List<AddResponse>)->Unit){
        repo.fetchAddress(spec,result)
    }
}