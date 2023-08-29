package com.target.supermarket.utilities

import android.content.Context
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavHostController
import com.target.supermarket.domain.models.LocalProduct

object CommonMethods {



    fun pop(navController:NavHostController){
        navController.popBackStack()
    }

    fun navigate(navController: NavHostController, route:String, from:String? = null){
        navController.navigate(route){
            from?.let {
                popUpTo(it){
                    inclusive = true
                }
            }
        }
    }

    fun appendUrl(url:String?):String? = if (url==null) null else Constants.BASE_URL +url

    fun Int.comma():String{
        return "%,d".format(this)
    }

    fun toggleCart(context:Context, isAdd:Boolean, p: LocalProduct){
        val manage = LocalBroadcastManager.getInstance(context)
        if (isAdd){
            val intent = Intent("ADD")
            intent.putExtra("product", p)
            manage.sendBroadcast(intent)
        }else{
            val intent = Intent("REMOVE")
            intent.putExtra("product", p)
            manage.sendBroadcast(intent)
        }
    }
}