package com.target.supermarket.presentation.viewModels

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.target.supermarket.domain.models.Address
import com.target.supermarket.domain.models.OrderDetails
import com.target.supermarket.domain.models.PaymentNumbers
import com.target.supermarket.domain.usecases.checkout.CheckOutUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckOutViewModel @Inject constructor(private val useCase: CheckOutUseCases, private val app:Application):
BaseViewModel<CheckOutContract.CheckOutState,CheckOutContract.CheckOutEvent, CheckOutContract.CheckOutEffect>(app){
    override fun initialiseState() = CheckOutContract.CheckOutState()

    init {
        onEvent()
        getNumbers()
        getAddress()
    }

    private fun onEvent(){
        viewModelScope.launch {
            _event.collectLatest {
                when(it){
                    is CheckOutContract.CheckOutEvent.OnPaymentMethodChanged -> {
                        val newList = mutableListOf<Pair<String, Boolean>>()
                        for (i in _state.value.methods){
                            if (i != it.method){
                                newList.add(i.copy(second = false))
                            }else{
                                newList.add(i.copy(second = true))
                            }
                        }
                        setState { copy(methods = newList,paymentMethod = it.method.first) }
                    }
                    is CheckOutContract.CheckOutEvent.OnPlaceOrder -> {
                        val orderDetails = OrderDetails(
                                price = it.price,
                                address = it.address,
                                contact = it.contact,
                                identification = it.identification,
                                status = "Placed",
                                items = it.items,
                                contactName = it.contactName,
                            )
                        setState { copy(showConfirmation = it.isConfirm, orderDetails = orderDetails ) }
                    }
                    is CheckOutContract.CheckOutEvent.OnAddNumber -> {
                        val number = it.number?.number ?: _state.value.newNumber
                        useCase.addPaymentNumber(number) { e ->
                            e?.let {
                                setState {
                                    copy(
                                        NumberError = e.message ?: "An Error has occurred"
                                    )
                                }
                            }
                            if (e == null) {
                                setState { copy(newNumber = "") }
                            }
                        }
                    }
                    is CheckOutContract.CheckOutEvent.OnNumberChanged -> {
                        setState { copy(newNumber = it.number) }
                    }
                    is CheckOutContract.CheckOutEvent.OnActiveStatusChanged -> {
                        useCase.updateNumber(it.number.copy(isActive = true))
                        setState { copy(activeNumber = it.number) }
                    }
                    CheckOutContract.CheckOutEvent.OnConfirm -> {
                        viewModelScope.launch {
                            setState { copy(paymentLoading = true, showConfirmation = false) }
                            useCase.placeOrder(_state.value.orderDetails){details, exception->
                                launch {
                                    delay(2000)
                                    setState { copy(paymentLoading = false) }
                                    details?.let { de->
                                        useCase.addOrder(de)
                                        setEffect { CheckOutContract.CheckOutEffect.OnPaymentComplete(de.id) }
                                    }
                                    exception?.let { setEffect { CheckOutContract.CheckOutEffect.OnOrderException(it) } }
                                }
                            }
                        }
                    }
                    is CheckOutContract.CheckOutEvent.OnEditOrderDetails -> {
                        setState { copy(orderDetails = it.details) }
                    }
                    is CheckOutContract.CheckOutEvent.CheckForNewNumberErrors -> {
                        if (_state.value.newNumber.isEmpty()){
                            it.result(Exception("Please Enter You Number"))
                        }else if (_state.value.newNumber.length!=10 || !_state.value.newNumber.startsWith('0')){
                            it.result(Exception("Invalid Number Entered"))
                        }else {
                            it.result(null)
                        }
                    }
                    is CheckOutContract.CheckOutEvent.ChangeDefaultAddress -> {
                        useCase.changeDefaultAddress(it.address)
                    }
                    is CheckOutContract.CheckOutEvent.OnEditAddress -> {
                        setState { copy(addressOnEdit = it.address, openAddressDialog = true) }
                    }
                    is CheckOutContract.CheckOutEvent.OnDeleteAddress -> {
                        useCase.deleteAddress(it.address)
                    }
                    is CheckOutContract.CheckOutEvent.OnToggleAddressDialog -> {
                        setState { copy(openAddressDialog = it.open) }
                    }
                    is CheckOutContract.CheckOutEvent.OnDeleteNumber -> {
                        useCase.updateNumber(it.number, isDelete = true)
                    }
                }
            }
        }
    }

    private fun getNumbers(){
        viewModelScope.launch {
            useCase.getPayNumbers().collectLatest {
                setState { copy(numbers = it.sortedByDescending { it.isActive }, activeNumber = if (_state.value.activeNumber==null){ it.firstOrNull{it.isActive} ?: it.firstOrNull()}else _state.value.activeNumber ) }
            }
        }
    }

    private fun getAddress(){
        viewModelScope.launch {
            useCase.getAddress().collectLatest {
                setState { copy(addresses = it)}
            }
        }
    }
}

class CheckOutContract{
    data class CheckOutState(
        val paymentLoading:Boolean = false,
        val showConfirmation:Boolean = false,
        val paymentMethod:String = "COD",
        val numbers:List<PaymentNumbers> = emptyList(),
        val newNumber:String = "",
        val activeNumber:PaymentNumbers? = null,
        val methods:List<Pair<String, Boolean>> = listOf(Pair("COD", false), Pair("Credit Card",false), Pair("MOMO Pay",false), Pair("Airtel Pay",true)),
        val NumberError: String = "",
        val orderDetails: OrderDetails? = null,
        val addresses:List<Address> = listOf(),
        val addressOnEdit:Address? = null,
        val openAddressDialog:Boolean = false
    ):ViewState

    sealed class CheckOutEvent:ViewEvent{
        data class OnPaymentMethodChanged(val method:Pair<String,Boolean>):CheckOutEvent()
        data class OnNumberChanged(val number:String):CheckOutEvent()
        data class OnActiveStatusChanged(val number:PaymentNumbers):CheckOutEvent()
        data class OnPlaceOrder(
            val isConfirm:Boolean,
            val price:Double,
            val address:String,
            val contact:String,
            val identification:String,
            val items:String,
            val contactName:String,
            ):CheckOutEvent()
        data class OnEditOrderDetails(val details:OrderDetails):CheckOutEvent()
        object OnConfirm:CheckOutEvent()
        data class OnAddNumber(val number: PaymentNumbers? = null):CheckOutEvent()
        data class OnDeleteNumber(val number: PaymentNumbers):CheckOutEvent()
        data class CheckForNewNumberErrors(val result:(Exception?)->Unit):CheckOutEvent()
        data class ChangeDefaultAddress(val address: Address):CheckOutEvent()
        data class OnEditAddress(val address: Address):CheckOutEvent()
        data class OnDeleteAddress(val address: Address):CheckOutEvent()
        data class OnToggleAddressDialog(val open:Boolean, val isNew:Boolean):CheckOutEvent()
    }

    sealed class CheckOutEffect:ViewEffect{
        data class OnPaymentComplete(val orderId:Int):CheckOutEffect()
        data class OnOrderException(val e:Exception):CheckOutEffect()
    }
}