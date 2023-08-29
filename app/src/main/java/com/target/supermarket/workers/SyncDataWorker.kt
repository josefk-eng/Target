package com.target.targetapp.workers

import android.content.Context
import android.widget.Toast
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.target.supermarket.data.local.room.TargetDB
import com.target.supermarket.data.remote.TargetApi
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.*

@HiltWorker
class SyncDataWorker @AssistedInject constructor (
    @Assisted context: Context,
    @Assisted parameters: WorkerParameters,
    var api: TargetApi,
    var db: TargetDB
):CoroutineWorker(context,parameters) {
//    @Inject lateinit var api: TargetApi
//    @Inject lateinit var db: TargetDB
    override suspend fun doWork(): Result {
        while (true){
            delay(5000)
            var error = ""
            try {
//                val response = api.getAllCategories()
//                if (response.isSuccessful){
//                    val body = response.body()
//                    body?.let {
//                        db.categoryDao().insertCategory(it)
//                    }
//                }else{
//                    error = "Response was not successful"
//                }
            }catch (e:Exception){
                error = e.message ?: "An Unknown Exception has been thrown"
            }finally {
                if (error.isNotEmpty()){
                    CoroutineScope(Dispatchers.Main).launch {
                        Toast.makeText(applicationContext,error,Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

}