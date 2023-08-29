package com.target.supermarket.domain.usecases.checkout

import com.target.supermarket.domain.usecases.main.AddAddress
import com.target.supermarket.domain.usecases.main.GetAddressGlobal

data class CheckOutUseCases(
    val addPaymentNumber: AddPaymentNumber,
    val getPayNumbers: GetPayNumbers,
    val updateNumber: UpdateNumber,
    val placeOrder: PlaceOrder,
    val addOrder: AddOrder,
    val getDistricts: GetDistricts,
    val getDivisions: GetDivisions,
    val getParish: GetParish,
    val getVillages: GetVillages,
    val getStreets: GetStreets,
    val addAddress: AddAddress,
    val getAddress: GetAddress,
    val changeDefaultAddress: ChangeDefaultAddress,
    val deleteAddress: DeleteAddress
    )
