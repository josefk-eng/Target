package com.target.supermarket.domain.repository

import com.target.supermarket.domain.models.CartItems
import com.target.supermarket.domain.models.LocalProduct
import com.target.supermarket.domain.models.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getProductById(id:Int):Flow<LocalProduct>
    fun getRelatedProducts(item: LocalProduct):Flow<List<LocalProduct>>
    fun getCartItemById(id: Int): Flow<CartItems>
}