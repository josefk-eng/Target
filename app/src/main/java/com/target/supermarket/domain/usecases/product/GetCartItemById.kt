package com.target.supermarket.domain.usecases.product

import com.target.supermarket.data.repositories.ProductsRepoImpl
import com.target.supermarket.domain.models.CartItems
import com.target.supermarket.domain.repository.MainRepo
import com.target.supermarket.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow

class GetCartItemById(private val repo: ProductRepository) {
    operator fun invoke(id:Int):Flow<CartItems>{
        return repo.getCartItemById(id)
    }
}