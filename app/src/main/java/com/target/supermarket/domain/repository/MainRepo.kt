package com.target.supermarket.domain.repository

import com.target.supermarket.domain.models.Address
import com.target.supermarket.domain.models.CartItems
import com.target.supermarket.domain.models.*
import kotlinx.coroutines.flow.Flow

interface MainRepo {
    suspend fun addToCart(item: LocalProduct)
    suspend fun removeFromCart(item: LocalProduct)
    fun getProductsInCart(): Flow<List<CartItems>>
    fun getProducts(): Flow<List<LocalProduct>>
    suspend fun getProductById(id:Int): Flow<LocalProduct>
    suspend fun fetchRemote(result:(Boolean, Exception?)->Unit)
    suspend fun getCategories(onError:(Exception)->Unit): Flow<List<Category>>
    suspend fun getActiveSeason():Flow<Season>
    fun getBanners():Flow<List<Banner>>
//    suspend fun updateProduct(item: Product)
    fun getAddresses(): Flow<List<Address>>
    suspend fun getProductsByCat(id: Int): Flow<List<LocalProduct>>
    fun getProductsByTags(tags: String): Flow<List<Product>>
    suspend fun fetchDeals():Flow<List<Product>>
    suspend fun fetchSpecial():Flow<List<Product>>
    suspend fun fetchRecommended():Flow<List<Product>>
    suspend fun fetchOffers():Flow<List<Product>>
    fun getNotifications(): Flow<List<TargetNotification>>
    suspend fun searchRemote(query:String): SearchResult
    suspend fun getCategoryById(id: Int):Category
    suspend fun getDeals():Flow<List<Product>>
    suspend fun getSpecialProducts(): Flow<List<Product>>
    suspend fun getProductOffer(): Flow<List<Product>>
    suspend fun addAddress(address: Address, isAuto:Boolean)
    fun collectOrders():Flow<List<OrderDetails>>
    suspend fun fetchAddress(spec: AddressSpec, result: (Exception?, List<AddResponse>) -> Unit)
    fun isFirstLaunch():Boolean
    suspend fun fetchBanners(callback: (Exception?) -> Unit)
    fun markUser()
    suspend fun getProductsByTagId(id:Int, callback: (Exception?) -> Unit): Flow<List<LocalProduct>>
    fun getTagById(id: Int): Flow<Tag>
    fun getBannerById(id: Int): Flow<Banner>
    fun getTagsByBannerId(id: Int): Flow<List<Tag>>
    fun fetchLocations(callback: (Exception) -> Unit)
    suspend fun clearCart()
    suspend fun insertNotes(notes: List<TargetNotification>)
    suspend fun notificationMenuClick(action: String, notification: TargetNotification)
}