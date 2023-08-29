package com.target.supermarket.domain.usecases.main

import com.target.supermarket.domain.models.LocalProduct
import com.target.supermarket.domain.models.Product
import com.target.supermarket.domain.repository.MainRepo
import kotlinx.coroutines.flow.Flow

class LoadDept(private val repo:MainRepo) {
    suspend operator fun invoke(id:Int):Flow<List<LocalProduct>>{
        return repo.getProductsByCat(id)
    }
}