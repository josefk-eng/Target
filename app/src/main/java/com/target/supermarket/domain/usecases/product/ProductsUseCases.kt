package com.target.supermarket.domain.usecases.product

data class ProductsUseCases(
    val getProduct: GetProductUseCase,
    val getRelatedProducts: getRelatedProducts,
    val getCartItemById: GetCartItemById
)
