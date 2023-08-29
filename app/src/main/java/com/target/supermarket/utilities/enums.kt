package com.target.supermarket.utilities

sealed class ItemsLoadingStatus{
    object Loading
    object Loaded
    data class Error(val error:String)
}
enum class Items{
    Categories,
    FeaturedCategories,
    Recommended,
    Deals,
    Special,
    Offer
}