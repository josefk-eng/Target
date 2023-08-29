package com.target.supermarket.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.target.supermarket.data.local.UserPreference
import com.target.supermarket.data.remote.TargetApi
import com.target.supermarket.utilities.Constants
import com.target.supermarket.utilities.InstallationUnique
import com.target.supermarket.domain.models.UserToken
import com.target.supermarket.domain.repository.MainRepo
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.Inet4Address

const val TAG:String = "TokenAnalyser"

@HiltWorker
class TokenAnalyser @AssistedInject constructor (
    @Assisted context: Context,
    @Assisted parameters: WorkerParameters,
    private val prefs: UserPreference,
    private val api: TargetApi,
    private val repo:MainRepo
):CoroutineWorker(context, parameters) {

    override suspend fun doWork(): Result {
        var times = 0
        var previouslyFailed = false
        while (true){
            delay(10000)
            try {
                if (
//                    Inet4Address.getByName("www.google.com").isReachable(5000)
//                    &&
                    api.checkAPI().isSuccessful){
                    onConnectionError("")
                    times=0
                    if (previouslyFailed){
//                        repo.getCategories()
//                        repo.getBanners()
//                        repo.getDeals()
//                        repo.fetchOffers()
//                        repo.fetchRecommended()
//                        repo.fetchSpecial()
                        previouslyFailed = false
                    }
                }else{
                    if (times>6){
                        times=0
                    }else{
                        times += 1
                    }
                    previouslyFailed = true

                }
            }catch (e:Exception)
            {
                Log.e(TAG, "InternetCheck Error: ", e)
                if (times>6){
                    times=0
                }else{
                    times += 1
                }
            }
            if (!prefs.isTokenOnServer()){
                val deviceToken = prefs.localDeviceToken()
                if (deviceToken.isNotEmpty()){
                    try {
                        InstallationUnique.id(applicationContext)?.let {
                            val response =  api.addToken(UserToken(deviceToken,it))
                            if (response.isSuccessful){
                                prefs.tokenSentRemotely(true)
                            }
                        }
                    }catch (e:Exception){
                        Log.e(TAG, "TokenSubmission Error: ", e)
                        onConnectionError(e.message ?: "An Error Has Occurred")
                    }
                }
            }
            else{
                if (times==2){
                    onConnectionError("No Active Internet Connection")
                }
            }
        }
    }

    private fun onConnectionError(error:String){
        CoroutineScope(Dispatchers.Main).launch {
            Constants.connectionError.value = error
        }
    }
}