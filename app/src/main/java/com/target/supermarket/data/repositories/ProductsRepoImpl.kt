package com.target.supermarket.data.repositories

import com.target.supermarket.data.local.room.TargetDB
import com.target.supermarket.data.local.room.daos.LocalProductDao
import com.target.supermarket.data.local.room.daos.ProductDao
import com.target.supermarket.data.remote.TargetApi
import com.target.supermarket.domain.models.CartItems
import com.target.supermarket.domain.models.LocalProduct
import com.target.supermarket.domain.models.Product
import com.target.supermarket.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow

class ProductsRepoImpl(private val db: TargetDB, private val api: TargetApi):ProductRepository {
    override fun getProductById(id: Int): Flow<LocalProduct> {
        return db.localProductDao().getProductById(id)
    }

    override fun getRelatedProducts(item: LocalProduct): Flow<List<LocalProduct>> {
        TODO("Not yet implemented")
    }

    override fun getCartItemById(id: Int): Flow<CartItems> {
        return db.cartItemDao().getItemByProductId(id)
    }
}