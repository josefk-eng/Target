package com.target.supermarket.domain.usecases.checkout

import com.target.supermarket.domain.models.District
import com.target.supermarket.domain.models.Division
import com.target.supermarket.domain.repository.CheckOutRepository
import com.target.supermarket.domain.repository.MainRepo
import kotlinx.coroutines.flow.Flow

class GetDivisions(private val repo: CheckOutRepository) {
    operator fun invoke(id:Int):Flow<List<Division>>{
        return repo.getDivisions(id)
    }
}