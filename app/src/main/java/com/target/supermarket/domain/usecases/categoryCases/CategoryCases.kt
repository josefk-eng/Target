package com.target.supermarket.domain.usecases.categoryCases

data class CategoryCases(
    val getProductsByTag: GetProductsByTagId,
    val getTagsByBannerId: GetTagsByBannerId,
    val getBannerById: GetBannerById
)
