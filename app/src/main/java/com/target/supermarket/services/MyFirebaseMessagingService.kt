package com.target.supermarket.services

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.target.supermarket.data.local.UserPreference
import com.target.supermarket.data.local.room.TargetDB
import com.target.supermarket.data.remote.TargetApi
import com.target.supermarket.utilities.InstallationUnique
import com.target.supermarket.domain.models.UserToken
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MyFirebaseMessagingService: FirebaseMessagingService() {
    @Inject lateinit var api: TargetApi
    @Inject lateinit var db: TargetDB
    @Inject lateinit var prefs: UserPreference
    private val coroutineException = CoroutineExceptionHandler { _, throwable ->
        prefs.tokenSentRemotely(false)
        makeToast(throwable.message ?: "An Unknown Error Occurred")
    }
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        prefs.saveDeviceToken(token)
        CoroutineScope(Dispatchers.IO).launch(coroutineException) {
            InstallationUnique.id(applicationContext)?.let {
                if (it.isNotEmpty()){
                    val response = api.addToken(UserToken(token,it))
                    if (response.isSuccessful){
                        prefs.tokenSentRemotely(true)
                        makeToast("User Token Created On The Server")
                    }else {
                        makeToast("Token Could Not Be Sent Remote")
                    }
                }else {
                    makeToast("Could not create unique installation ID")
                }
            }
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d("Firebase Message", message.data.toString())
        CoroutineScope(Dispatchers.IO).launch(coroutineException) {
            message.notification?.body?.toInt()?.let {id->
                message.notification?.title?.let { table->
                    if (table.equals("category", ignoreCase = true)){
                        val cachedCat = db.categoryDao().getCategoryByID(id=id)
                        val response = api.getCategoryByID(id=id)
                        if (response.isSuccessful){
                            response.body()?.let {cat->
                                db.categoryDao().insertCategory(cat)
                            }
                            if (cachedCat == null) {
                                val itemsResponse = api.getItemsByCategory(id)
                                if (itemsResponse.isSuccessful) {
                                    for (prod in itemsResponse.body() ?: emptyList()) {
                                        db.localProductDao().insertProduct(prod)
                                    }
                                }
                            }
                        }else{
                            throw Exception("Un able to add category")
                        }
                    }else if (table.equals("item", ignoreCase = true)){
                       val response = api.getItemByID(id=id)
                        if (response.isSuccessful){
                            response.body()?.let {item->
//                                db.itemDao().insertItem(item)
                            }
                        }else{
                            throw Exception("Un able to add Item")
                        }
                    }else if(table.contains("banner", ignoreCase = true)){
                        if (table.contains("_")){
                            db.bannerDao().getBannerByID(id).collectLatest {
                                db.bannerDao().deleteBanner(it)
                            }
                        }else{
                            val response = api.getBannerById(id=id)
                            if (response.isSuccessful){
                                response.body()?.let {banner->
                                    db.bannerDao().insertBanner(banner)
                                }
                            }else{
                                throw Exception("Un able to add banner")
                            }
                        }
                    }else if(table.equals("season", ignoreCase = true)){
                        val response = api.getSeason(id=id)
                        if (response.isSuccessful){
                            response.body()?.let {season->
                                db.seasonDao().insertSeason(season)
                            }
                        }else{
                            throw Exception("Un able to add season")
                        }
                    }
                }
            }
        }
    }



    private fun makeToast(msg:String){
        CoroutineScope(Dispatchers.Main).launch {
            Toast.makeText(applicationContext,msg,Toast.LENGTH_SHORT).show()
        }
    }

    companion object{
        fun getToken(context: Context):String?{
            return context.getSharedPreferences("_", MODE_PRIVATE).getString("fb","")
        }
        fun checkGooglePlayServices(context: Context): Boolean {
            // 1
            val status = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context)
            // 2
            return status == ConnectionResult.SUCCESS
        }
    }
}

//eu7gPhdmQPafTQB6UWw6xa:APA91bHLe-_VwhIf-rRax5ACrzQ59rR8UN7czYHfSnT0OtbUOZdfh_oAaBVTeaNRgPW23joKudZywJaEoNZO3FDnnZpwGzYM-s4g_av8y5Rgf00z76ybvkMMGmNfIhauAKYaAYKMWHEA