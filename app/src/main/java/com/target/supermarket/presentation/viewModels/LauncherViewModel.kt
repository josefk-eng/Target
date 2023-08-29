package com.target.supermarket.presentation.viewModels

import android.app.Application
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.target.supermarket.domain.usecases.launcherCases.LauncherCases
import com.target.supermarket.workers.TokenAnalyser
import com.target.supermarket.workers.UserMonitorWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LauncherViewModel @Inject constructor(private val app:Application, private val cases:LauncherCases):
BaseViewModel<LauncherContract.LauncherState, LauncherContract.LauncherEvent, LauncherContract.LauncherEffect>(app){
    override fun initialiseState() = LauncherContract.LauncherState()

    init {
        startWorker()
        checkFirstLaunch()
        fetchBanners()
        onEvent()
    }

    private fun checkFirstLaunch(){
        setState { copy(onFirstLaunch = cases.checkFirstLaunch()) }
    }

    private fun fetchBanners(){
        viewModelScope.launch {
            cases.fetchBanners{
                setEffect { LauncherContract.LauncherEffect.OnLoadingDone(it) }
                fetchLocations()
            }
        }
    }

    private fun fetchLocations(){
        viewModelScope.launch {
            cases.fetchLocations{

            }
        }
    }

    private fun onEvent(){
        viewModelScope.launch {
            _event.collectLatest {
                when(it){
                    LauncherContract.LauncherEvent.MoveOnRegardless -> {
                        delay(2000)
                        setEffect { LauncherContract.LauncherEffect.MoveOnRegardless }
                    }
                    is LauncherContract.LauncherEvent.OnExceptionChanged -> {
                        setState { copy(remoteException = it.exception) }
                    }
                }
            }
        }
    }

    private fun startWorker() {
        val request = OneTimeWorkRequestBuilder<UserMonitorWorker>().build()
        val workManager = WorkManager.getInstance(app)
        workManager.enqueue(request)
    }
}

class LauncherContract{
    data class LauncherState(
        val onFirstLaunch:Boolean = false,
        val remoteException:Exception? = null
    ):ViewState

    sealed class LauncherEvent:ViewEvent{
        data class OnExceptionChanged(val exception: Exception?):LauncherEvent()
        object MoveOnRegardless:LauncherEvent()
    }

    sealed class LauncherEffect:ViewEffect{
        data class OnLoadingDone(val exception:Exception?):LauncherEffect()
        object MoveOnRegardless:LauncherEffect()
    }
}