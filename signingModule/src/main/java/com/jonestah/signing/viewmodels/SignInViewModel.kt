package com.jonestah.signing.viewmodels

import android.app.Application


class SignInViewModel(app:Application):BaseViewModel<
        SigningInContract.Sstate, SigningInContract.SEvent, SigningInContract.SEffect>(app) {
    override fun initialiseState()=SigningInContract.Sstate()

}

class SigningInContract{
    data class Sstate(val token:String = ""):ViewState
    sealed class SEvent:ViewEvent{}
    sealed class SEffect:ViewEffect{}
}