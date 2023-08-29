package com.target.supermarket.domain.usecases.checkout

import com.target.supermarket.domain.models.District
import com.target.supermarket.domain.models.Village
import com.target.supermarket.domain.repository.CheckOutRepository
import com.target.supermarket.domain.repository.MainRepo
import kotlinx.coroutines.flow.Flow

class GetVillages(private val repo: CheckOutRepository) {
    operator fun invoke(id:Int):Flow<List<Village>>{
        return repo.getVillages(id)
    }
}