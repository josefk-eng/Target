package com.target.supermarket.presentation.navigation

import com.target.supermarket.R

sealed class Screen(val route:String){
    object Home: Screen("home")
    object OnBord: Screen("OnBord")
    object Orders: Screen("order")
    object Category: Screen("category?bannerId={bannerId}&catId={catId}&banner={banner}"){
        fun passArg(bannerId:Int?=null, catId:Int?=null):String{
            return "category?bannerId=$bannerId&catId=$catId"
        }
    }
    object Cart: Screen("cart")
    object CheckOut: Screen("checkout")
    object Track: Screen("track/{isNew}/{orderId}"){
        fun passArg(isNew:Boolean = false, orderId:Int?):String{
            return "track?isNew=$isNew&orderId=$orderId"
        }
    }
    object Launcher: Screen("launch")
    object Final: Screen("final")
    object Product: Screen("product?id={id}"){
        fun passArg(id:Int):String{
            return "product?id=$id"
        }
    }
    object OrderTracker: Screen("orderTracker")
}

const val TOORDER = "toOrderList"
sealed class BottomScreenItem(val title:String, val icon: Int, val route: String){
    object Home:BottomScreenItem("Home", R.drawable.home, "home/{$TOORDER}/{orderId}"){
        fun passArg(to:Boolean = false, orderId: Int?):String{
            return this.route.replace(oldValue = "{$TOORDER}/{orderId}", newValue = "$to/$orderId")
        }
    }
    object Order:BottomScreenItem("Orders", R.drawable.processed, "order/{isNew}/{orderId}"){
        fun passArg(isNew: Boolean,orderId: Int?):String{
            return "order/$isNew/$orderId"
        }
    }
}
