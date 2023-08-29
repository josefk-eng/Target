package com.target.supermarket.presentation.viewModels

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

interface ViewState
interface ViewEvent
interface ViewEffect

abstract class BaseViewModel<UIState:ViewState, Event:ViewEvent,Effect:ViewEffect>(app:Application):AndroidViewModel(app) {
    private val initialState by lazy { initialiseState() }
    abstract fun initialiseState():UIState

    protected val _state = mutableStateOf(initialState)
    val state:State<UIState> = _state

    protected val _event = MutableSharedFlow<Event>()

    protected val _effect = Channel<Effect>()
    val effect:Flow<Effect> = _effect.receiveAsFlow()

    fun setEvent(event: Event) {
        viewModelScope.launch {
            _event.emit(event)
        }
    }

    protected fun setState(reducer:UIState.()->UIState){
        val newState = state.value.reducer()
        _state.value = newState
    }

    protected fun setEffect(builder:()->Effect){
        val newEffect = builder()
        viewModelScope.launch { _effect.send(newEffect) }
    }
}