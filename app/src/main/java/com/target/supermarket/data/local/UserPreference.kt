package com.target.supermarket.data.local

import android.app.Application
import com.google.firebase.messaging.FirebaseMessagingService


class UserPreference(app:Application) {
    private val prefs = app.getSharedPreferences("_", FirebaseMessagingService.MODE_PRIVATE)
    companion object{
        const val TOKEN_SAVED_KEY = "tokenSaved"
        const val TOKEN_KEY = "tokenKey"
        const val NEW_USER = "new_user"
    }

    fun tokenSentRemotely(isTokenSaved:Boolean){
        prefs.edit().apply {
            putBoolean(TOKEN_SAVED_KEY,isTokenSaved)
            apply()
        }
    }

    fun saveDeviceToken(token:String){
        prefs.edit().apply {
            putString(TOKEN_KEY,token)
            apply()
        }
    }

    fun changeUserStatus(status:Boolean){
        prefs.edit().apply {
            putBoolean(NEW_USER,status)
            apply()
        }
    }

    fun localDeviceToken():String = prefs.getString(TOKEN_KEY,"") ?: ""
    fun isTokenOnServer():Boolean = prefs.getBoolean(TOKEN_SAVED_KEY, false)
    fun isNewUser():Boolean = prefs.getBoolean(NEW_USER, true)

}