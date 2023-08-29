package com.target.supermarket.data.repositories

import android.os.Parcel
import com.target.supermarket.data.local.room.TargetDB
import com.target.supermarket.data.remote.TargetApi
import com.target.supermarket.domain.models.*
import com.target.supermarket.domain.repository.CheckOutRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class CheckOutRepoImpl(private val api: TargetApi, private val db: TargetDB):CheckOutRepository {

    override suspend fun addAddress(address: Address, isAuto:Boolean) {
        if (isAuto){
            db.addressDao().getAutoAddress().collectLatest {
                db.addressDao().insertAddress(it.copy(village = address.village, street = address.street, phoneNumber = address.phoneNumber, isDefault = true))
            }
        }else {
            db.addressDao().insertAddress(address.copy(isDefault = true))
        }
    }

    override suspend fun addNumber(number: String, result: (Exception?) -> Unit) {
        val telecom = getTelecom(number.dropLast(7))
        if (telecom!=null){
            updateNumber(PaymentNumbers(number = number, highestAmount = 0.0, transCount = 0 ), isDelete = false)
            result(null)
        }else {
            result(Exception("Invalid Number"))
        }
    }

    override fun getAllPayNumbers(): Flow<List<PaymentNumbers>> {
        return db.numberDao().getAllNumbers()
    }

    override suspend fun updateNumber(number: PaymentNumbers, isDelete:Boolean) {
        if (isDelete){
            db.numberDao().delete(number)
        }else{
            val numbers = db.numberDao().getAllNumbers().first()
            db.numberDao().insertNumber(number)
            for (n in numbers){
                if (n.number!=number.number){
                    db.numberDao().insertNumber(n.copy(isActive = false))
                }
            }
        }
    }

    override suspend fun placeOrder(details: OrderDetails, result: (OrderDetails?,Exception?) -> Unit) {
        try {
            val response = api.order(details)
            if (response.isSuccessful){
                val res = response.body() as OrderDetails
                db.orderDao().insertOrder(res)
                result(response.body(), null)
                delay(5000)
                db.notificationDao().insertAll(listOf(
                    TargetNotification(
                        title = "Order with reference ${res.id} has been received",
                        thumbnail = "",
                        image = "",
                        description = "Your Order with has been received and under processing we will be notifying you by sms",
                        date = "",
                        read = false,
                        target = "${res.id}",
                        destination = ""
                    )
                ))
            }else{
                result(null, Exception("Oder was not successful"))
            }
        }catch (e:Exception){
            result(null, Exception("An Error has Occurred during ordering"))
        }
    }

    override fun getDistricts(): Flow<List<District>> {
        return db.districtDao().getAllDistricts()
    }

    override fun getDivisions(id: Int): Flow<List<Division>> {
        return db.divisionDao().getDivisionByDistrict(id)
    }

    override fun getParish(id: Int): Flow<List<Parish>> {
        return db.parishDao().getAllParishByDivision(id)
    }

    override fun getVillages(id: Int): Flow<List<Village>> {
        return db.villageDao().getAllVillageByParish(id)
    }

    override fun getStreet(id: Int): Flow<List<Street>> {
        return db.streetDao().getAllStreetByVillage(id)
    }

    override fun getAddresses(): Flow<List<Address>> {
        return db.addressDao().getAllAddress()
    }

    override suspend fun changeDefaultAddress(address: Address) {
        val addresses = db.addressDao().getAllAddress().first()
        for (a in addresses){
            if (a==address){
                db.addressDao().insertAddress(a.copy(isDefault = true))
            }else{
                db.addressDao().insertAddress(a.copy(isDefault = false))
            }
        }
    }

    override suspend fun deleteAddress(address: Address) {
        db.addressDao().deleteAddress(address)
    }

    private fun getTelecom(code: String): String? {
       return if(code.endsWith("70") || code.endsWith("75")){
            "airtel"
        }else if (code.endsWith("77") || code.endsWith("78")){
            "mtn"
        }else{
            null
        }
    }
}