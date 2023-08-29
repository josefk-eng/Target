package com.target.supermarket.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.target.supermarket.data.local.UserPreference
import com.target.supermarket.data.remote.TargetApi
import com.target.supermarket.domain.models.Customer
import com.target.supermarket.domain.repository.MainRepo
import com.target.supermarket.utilities.InstallationUnique
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.delay

@HiltWorker
class UserMonitorWorker  @AssistedInject constructor (
    @Assisted context: Context,
    @Assisted parameters: WorkerParameters,
    private val prefs: UserPreference,
    private val api: TargetApi,
    private val repo: MainRepo
): CoroutineWorker(context, parameters) {

    override suspend fun doWork(): Result {
        while (true){
            try {
                if (prefs.isNewUser() && prefs.localDeviceToken().isEmpty()){
                    InstallationUnique.id(applicationContext)?.let {
                        val task = api.addUser(Customer(deviceId = it, token = it))
                        if (task.isSuccessful){
                            prefs.saveDeviceToken(it)
                        }else{

                        }
                    }
                }else{

                }
            }catch (e:Exception){
                Log.e(TAG, "UserMonitor Error: ", e)
            }
            delay(5000)
        }
    }

}