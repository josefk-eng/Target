package com.target.supermarket.domain.usecases.main

import android.location.Address
import com.target.supermarket.domain.repository.MainRepo

class AddLocation(private val repo:MainRepo) {
    suspend operator fun invoke(address: Address, isAuto:Boolean){
        val localAdd = com.target.supermarket.domain.models.Address(
            village = address.thoroughfare,
            street = address.subThoroughfare,
        )
//        repo.addAddress(localAdd, isAuto = isAuto)
    }
}