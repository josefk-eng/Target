package com.target.supermarket.domain.repository

import com.target.supermarket.domain.models.*
import kotlinx.coroutines.flow.Flow

interface CheckOutRepository {
    suspend fun addAddress(address: Address, isAuto:Boolean)
    suspend fun addNumber(number:String, result: (Exception?) -> Unit)
    fun getAllPayNumbers():Flow<List<PaymentNumbers>>
    suspend fun updateNumber(number:PaymentNumbers, isDelete:Boolean)
    suspend fun placeOrder(details: OrderDetails, result: (OrderDetails?,Exception?) -> Unit)
    fun getDistricts(): Flow<List<District>>
    fun getDivisions(id: Int): Flow<List<Division>>
    fun getParish(id: Int): Flow<List<Parish>>
    fun getVillages(id: Int): Flow<List<Village>>
    fun getStreet(id: Int): Flow<List<Street>>
    fun getAddresses():Flow<List<Address>>
    suspend fun changeDefaultAddress(address: Address)
    suspend fun deleteAddress(address: Address)
}