package com.target.supermarket.presentation.viewModels

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.target.supermarket.domain.usecases.onBoardingCases.OnBoardingCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(private val app:Application, private  val case:OnBoardingCases):
BaseViewModel<OnBoardingContract.OnBoardingState, OnBoardingContract.OnBoardingEvent, OnBoardingContract.OnBoardingEffect>(app){
    override fun initialiseState() = OnBoardingContract.OnBoardingState()

    init {
        onEvent()
    }


    private fun onEvent(){
        viewModelScope.launch {
            _event.collectLatest {
                when(it){
                    OnBoardingContract.OnBoardingEvent.MarkUser -> {
                        case.markUser()
                    }
                }
            }
        }
    }
}


class OnBoardingContract{

    data class OnBoardingState(
        val remoteError:Exception? = null
    ):ViewState

    sealed class OnBoardingEvent:ViewEvent{
        object MarkUser:OnBoardingEvent()
    }

    sealed class OnBoardingEffect:ViewEffect{

    }
}