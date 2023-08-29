package com.target.supermarket.presentation.viewModels

import android.app.Application
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.viewModelScope
import com.target.supermarket.domain.models.*
import com.target.supermarket.domain.usecases.checkout.CheckOutUseCases
import com.target.supermarket.domain.usecases.checkout.GetDivisions
import com.target.supermarket.domain.usecases.main.MainUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeliveryInfoViewModel @Inject constructor(private val cases: CheckOutUseCases, private val app:Application):
BaseViewModel<DeliveryInfoContact.DeliveryInfoState,DeliveryInfoContact.InfoEvent, DeliveryInfoContact.InfoEffect>(app){
    override fun initialiseState() = DeliveryInfoContact.DeliveryInfoState()
    init {
        onEvent()
    }

    private fun onEvent(){
        viewModelScope.launch {
            _event.collectLatest {
                when(it){
                    is DeliveryInfoContact.InfoEvent.OnConfPasswordChanged -> {
                        setState { copy(confPassword = it.confPassword) }
                    }
                    is DeliveryInfoContact.InfoEvent.OnContactChanged -> {
                        setState { copy(contact = it.contact) }
                        if (it.contact.isEmpty()){
                            setState { copy(contactError = "This Field cannot be empty") }
                        }else if(!Patterns.PHONE.matcher(it.contact).matches() || !it.contact.startsWith('0') || it.contact.length != 10){
                            setState { copy(contactError = "Invalid Phone Number") }
                        }else {
                            setState { copy(contactError = "") }
                        }
                    }
                    is DeliveryInfoContact.InfoEvent.OnEmailChanged -> {
                        setState { copy(email = it.email) }
                        if (it.email.isEmpty()){
                            setState { copy(emailError = "This Field cannot be empty") }
                        }else if(!Patterns.EMAIL_ADDRESS.matcher(it.email).matches()){
                            setState { copy(emailError = "Invalid Email Address") }
                        } else {
                            setState { copy(emailError = "") }
                        }
                    }
                    is DeliveryInfoContact.InfoEvent.OnNamesChanged -> {
                        setState { copy(names = it.names) }
                        if (it.names.isEmpty()){
                            setState { copy(nameError = "This Field cannot be empty") }
                        }else {
                            setState { copy(nameError = "") }
                        }
                    }
                    is DeliveryInfoContact.InfoEvent.OnPasswordChanged -> {
                        setState { copy(password = it.password) }
                    }
                    is DeliveryInfoContact.InfoEvent.OnStreetChanged -> {
                        setState { copy(street = _state.value.streets.firstOrNull{d-> d.name==it.street}) }
                    }
                    is DeliveryInfoContact.InfoEvent.OnUserNameChanged -> {
                        setState { copy(userName = it.userName) }
                    }
                    DeliveryInfoContact.InfoEvent.OnSaveDeliveryInfo -> {
                        delay(2000)
                        cases.addAddress(
                            Address(
                                contactName = _state.value.names,
                                phoneNumber = _state.value.contact,
                                email = _state.value.email,
                                street = _state.value.street?.name ?: "",
                                district = _state.value.district?.name ?: "",
                                division = _state.value.division?.name ?: "",
                                parish = _state.value.parish?.name ?: "",
                                village = _state.value.village?.name ?: "",
                                roomNo = _state.value.roomNo,
                            )
                        )
                        setEffect { DeliveryInfoContact.InfoEffect.OnDetailsSaved(numbers = PaymentNumbers(number = _state.value.contact, isActive = true)) }
                    }
                    is DeliveryInfoContact.InfoEvent.OnDistrictChanged -> {
                        setState { copy(district = _state.value.districts.firstOrNull{d-> d.name==it.district}) }
                        setEvent(DeliveryInfoContact.InfoEvent.OnGetDivision(_state.value.district?.id ?: 0))
                    }
                    is DeliveryInfoContact.InfoEvent.OnDivisionChanged -> {
                        setState { copy(division = _state.value.divisions.firstOrNull{d-> d.name==it.division}) }
                        setEvent(DeliveryInfoContact.InfoEvent.OnGetParish(_state.value.division?.id ?: 0))
                    }
                    is DeliveryInfoContact.InfoEvent.OnParishChanged -> {
                        setState { copy(parish = _state.value.parishes.firstOrNull{d-> d.name==it.parish}) }
                        setEvent(DeliveryInfoContact.InfoEvent.OnGetVillage(_state.value.parish?.id ?: 0))
                    }
                    is DeliveryInfoContact.InfoEvent.OnVillageChanged -> {
                        setState { copy(village = _state.value.villages.firstOrNull{d-> d.name==it.village}) }
                        setEvent(DeliveryInfoContact.InfoEvent.OnGetStreet(_state.value.village?.id ?: 0))
                    }
                    is DeliveryInfoContact.InfoEvent.OnRoomNoChanged -> {
                        setState { copy(roomNo = it.roomNo) }
                    }
                    DeliveryInfoContact.InfoEvent.CheckForErrors -> {
                        if (_state.value.names.isEmpty()){
                            setState { copy(nameError = "This Field Cannot be empty") }
                        }else if(_state.value.email.isEmpty()){
                            setState { copy(emailError = "This Field Cannot be empty") }
                        }else if (_state.value.contact.isEmpty()){
                            setState { copy(contactError = "This Field Cannot be empty") }
                        }else if (!Patterns.EMAIL_ADDRESS.matcher(_state.value.email).matches()){
                            setState { copy(emailError = "Invalid email address") }
                        }
                        else if(!Patterns.PHONE.matcher(_state.value.contact).matches()  || !_state.value.contact.startsWith('0') || _state.value.contact.length != 10){
                            setState { copy(contactError = "Invalid phone contact") }
                        }else {
                            setState { copy(nameError = "", emailError = "", contactError = "") }
                        }
                        val check = _state.value.nameError.isNotEmpty() || _state.value.contactError.isNotEmpty() || _state.value.emailError.isNotEmpty()
                        setState { copy(inputError = if(check) "Some Errors Were Encountered" else "") }
                        if (!check){
                            setEffect { DeliveryInfoContact.InfoEffect.OnFirstPartOK }
                        }
                    }
                    DeliveryInfoContact.InfoEvent.CheckForAdErrors -> {
                        val err = "This Field cannot be empty"
                        if (_state.value.district == null){
                            setState { copy(districtError = err) }
                        }else if(_state.value.division == null) {
                            setState { copy(divisionError = err) }
                        }else if(_state.value.parish == null) {
                            setState { copy(parishError = err) }
                        }else if(_state.value.village == null) {
                            setState { copy(villageError = err) }
                        }else if(_state.value.street == null) {
                            setState { copy(streetError = err) }
                        }
                        val check = _state.value.district==null || _state.value.division==null || _state.value.parish==null || _state.value.village==null || _state.value.street==null
                        if(!check){
                            setEffect { DeliveryInfoContact.InfoEffect.OnSecondPartOK }
                        }else {
                            setState { copy(addError = if(check) "Some Errors Were Encountered" else "") }
                        }
                    }
                    DeliveryInfoContact.InfoEvent.OnGetDistricts -> {
                        cases.getDistricts().collectLatest { d->
                            setState { copy(districts = d) }
                            d.firstOrNull()?.let {myD->
                                setEvent(DeliveryInfoContact.InfoEvent.OnDistrictChanged(myD.name))
                            }
                        }
                    }
                    is DeliveryInfoContact.InfoEvent.OnGetDivision -> {
                        cases.getDivisions(it.id).collectLatest { di->
                            setState { copy(divisions = di) }
                            Log.e("GETTING DIVISION", "GETTING DIVISION LOG ", )
                        }
                    }
                    is DeliveryInfoContact.InfoEvent.OnGetParish -> {
                        cases.getParish(it.id).collectLatest { p->
                            setState { copy(parishes = p) }
                        }
                    }
                    is DeliveryInfoContact.InfoEvent.OnGetStreet -> {
                        cases.getStreets(it.id).collectLatest { s->
                            setState { copy(streets = s) }
                        }
                    }
                    is DeliveryInfoContact.InfoEvent.OnGetVillage -> {
                        cases.getVillages(it.id).collectLatest { v->
                            setState { copy(villages = v) }
                        }
                    }
                    is DeliveryInfoContact.InfoEvent.OnEditAddress -> {
                        _state.value = _state.value.copy(
                            names = it.add.contactName,
                            email = it.add.email,
                            contact = it.add.phoneNumber,
                            district = _state.value.districts.firstOrNull { d-> d.name == it.add.district },
                            division = _state.value.divisions.firstOrNull { d-> d.name==it.add.division },
                            parish = _state.value.parishes.firstOrNull { p-> p.name==it.add.parish },
                            village = _state.value.villages.firstOrNull { v-> v.name==it.add.village },
                            street = _state.value.streets.firstOrNull { s-> s.name == it.add.street },
                            roomNo = it.add.roomNo
                        )
                    }
                }
            }
        }
    }
}

class DeliveryInfoContact{
    data class DeliveryInfoState(
        val names:String = "",
        val nameError:String = "",
        val email:String = "",
        val emailError:String = "",
        val roomNo:String = "",
        val userName: String = "",
        val contact: String = "",
        val contactError: String = "",
        val password:String = "",
        val confPassword:String = "",
        val infoError:String = "",
        val inputError:String = "",
        val addError:String = "",
        val districts: List<District> = emptyList(),
        val divisions: List<Division> = emptyList(),
        val parishes: List<Parish> = emptyList(),
        val villages: List<Village> = emptyList(),
        val streets: List<Street> = emptyList(),
        val district:District? = null,
        val districtError:String = "",
        val division:Division? = null,
        val divisionError:String = "",
        val parish:Parish? = null,
        val parishError:String = "",
        val village:Village? = null,
        val villageError:String = "",
        val street:Street? = null,
        val streetError:String = "",
    ):ViewState


    sealed class InfoEffect:ViewEffect{
        data class OnDetailsSaved(val numbers: PaymentNumbers):InfoEffect()
        object OnFirstPartOK:InfoEffect()
        object OnSecondPartOK:InfoEffect()
    }

    sealed class InfoEvent:ViewEvent{
        data class OnNamesChanged(val names: String): InfoEvent()
        data class OnEmailChanged(val email: String): InfoEvent()
        data class OnUserNameChanged(val userName: String): InfoEvent()
        data class OnRoomNoChanged(val roomNo: String): InfoEvent()
        data class OnPasswordChanged(val password: String): InfoEvent()
        data class OnConfPasswordChanged(val confPassword: String): InfoEvent()
        data class OnContactChanged(val contact: String): InfoEvent()
        data class OnDistrictChanged(val district: String): InfoEvent()
        data class OnDivisionChanged(val division: String): InfoEvent()
        data class OnParishChanged(val parish: String): InfoEvent()
        data class OnVillageChanged(val village: String): InfoEvent()
        data class OnStreetChanged(val street: String): InfoEvent()
        object OnSaveDeliveryInfo:InfoEvent()
        object CheckForErrors:InfoEvent()
        object CheckForAdErrors:InfoEvent()
        object OnGetDistricts:InfoEvent()
        data class OnGetDivision(val id:Int):InfoEvent()
        data class OnGetParish(val id:Int):InfoEvent()
        data class OnGetVillage(val id:Int):InfoEvent()
        data class OnGetStreet(val id:Int):InfoEvent()
        data class OnEditAddress(val add:Address):InfoEvent()

    }
}




//is DeliveryInfoContact.InfoEvent.OnFetchAddress -> {
//    val spec = AddressSpec(it.domain,it.quota)
//    useCases.fetchAddress(spec){e,r->
//        e?.let {
//            setState { copy(infoError = it.message ?: "Something went wrong") }
//        }
//        if (r.isNotEmpty()){
//            when(it.domain){
//                "district" -> {
//                    val ds = r.map { d->
//                        District(id = d.id, name = d.name, isActive = d.isActive)
//                    }
//                    setState { copy(districts = ds, district = ds.firstOrNull()) }
////                                        setEvent(DeliveryInfoContact.InfoEvent.OnFetchAddress("division", _state.value.district?.id ?: 0))
//                }
//                "division" -> {
//                    val ds = r.map { d->
//                        Division(id = d.id, name = d.name, isActive = d.isActive, district = _state.value.district?.id ?: 0)
//                    }
//                    setState { copy(divisions = ds) }
////                                        setEvent(DeliveryInfoContact.InfoEvent.OnFetchAddress("parish", _state.value.division?.id ?: 0))
//                }
//                "parish" -> {
//                    val ps = r.map { d->
//                        Parish(id = d.id, name = d.name, isActive = d.isActive, division = _state.value.division?.id ?: 0)
//                    }
//                    setState { copy(parishes = ps) }
////                                        setEvent(DeliveryInfoContact.InfoEvent.OnFetchAddress("village", _state.value.parish?.id ?: 0))
//                }
//                "village" -> {
//                    val vs = r.map { d->
//                        Village(id = d.id, name = d.name, isActive = d.isActive, parish = _state.value.parish?.id ?: 0)
//                    }
//                    setState { copy(villages = vs) }
////                                        setEvent(DeliveryInfoContact.InfoEvent.OnFetchAddress("street", _state.value.village?.id ?: 0))
//                }
//                "street" -> {
//                    val st = r.map { d->
//                        Street(id = d.id, name = d.name, isActive = d.isActive, village = _state.value.village?.id ?: 0)
//                    }
//                    setState { copy(streets = st) }
//                }
//            }
//        }
//    }
//}