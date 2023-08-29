package com.target.supermarket.presentation.navigation

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.target.supermarket.presentation.cart.Cart
import com.target.supermarket.presentation.checkout.ThanksScreen
import com.target.supermarket.presentation.home.HomeScreen
import com.target.supermarket.presentation.ordertracker.OrderTracking
import com.target.supermarket.presentation.product.ProductScreen
import com.target.supermarket.presentation.viewModels.CommonViewModel

@Composable
fun BottomScreenNav(
    modifier: Modifier,
    navController: NavHostController,
    viewModel: CommonViewModel,
    startDestination: String,
    topNavController:NavHostController
) {
    NavHost(modifier = modifier, navController = navController, startDestination = startDestination){

        composable(route = BottomScreenItem.Home.route){
            HomeScreen(navController = navController, topNavHost = topNavController, viewModel = viewModel)
        }
        composable(route = Screen.Final.route){
            ThanksScreen(navController = navController, viewModel = viewModel)
        }
        composable(route = BottomScreenItem.Order.route,
            arguments = listOf(navArgument("isNew"){ type = NavType.BoolType }, navArgument("orderId"){
            type = NavType.IntType })
        ){
            val isNew = it.arguments?.getBoolean("isNew")!!
            val orderID = it.arguments?.getInt("orderId")
            OrderTracking(navController = navController, cViewModel = viewModel, isNew = isNew, orderID = orderID)
        }
    }
}