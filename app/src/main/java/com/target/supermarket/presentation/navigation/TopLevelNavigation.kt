package com.target.supermarket.presentation.navigation

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.target.supermarket.R
import com.target.supermarket.presentation.cart.Cart
import com.target.supermarket.presentation.category.NewCategory
import com.target.supermarket.presentation.checkout.CheckOut
import com.target.supermarket.presentation.checkout.ThanksScreen
import com.target.supermarket.presentation.launcher.LauncherScreen
import com.target.supermarket.presentation.onboarding.OnBoardingScreen
import com.target.supermarket.presentation.ordertracker.OrderTracking
import com.target.supermarket.presentation.product.ProductScreen
import com.target.supermarket.presentation.viewModels.CommonViewModel

@Composable
fun TopLevelNavigation(navController: NavHostController, viewModel: CommonViewModel, modifier:Modifier) {
    NavHost(
        modifier = modifier.paint(
            painter = painterResource(id = R.drawable.background),
            contentScale = ContentScale.FillBounds
        ),
        navController = navController, startDestination = Screen.Launcher.route
    ){
        composable(route = Screen.Launcher.route){
            LauncherScreen(navController = navController)
        }

        composable(route = Screen.OnBord.route){
            OnBoardingScreen(navController = navController)
        }

        composable(
            route = BottomScreenItem.Home.route,
            arguments = listOf(navArgument(TOORDER){
                type = NavType.BoolType
            }, navArgument("orderId"){
                type = NavType.IntType
            })
        ){
            val to = it.arguments?.getBoolean(TOORDER) ?: false
            val orderId = it.arguments?.getInt("orderId")
            MainScreen(viewModel = viewModel, topController = navController, orderId = orderId)
        }
        composable(route = Screen.Cart.route,
        deepLinks = listOf(
            navDeepLink {
                uriPattern = "cart://cartitems.com"
            }
        )
        ){
            Cart(navController = navController, viewModel = viewModel)
        }
        composable(route = Screen.CheckOut.route){
            CheckOut(navController = navController, viewModel = viewModel)
        }
        composable(route = Screen.Final.route){
            ThanksScreen(navController = navController, viewModel=viewModel)
        }
        composable(route = Screen.Track.route, arguments = listOf(navArgument("isNew"){ type = NavType.BoolType }, navArgument("orderId"){
            type = NavType.IntType
        })
        ){
            val isNew = it.arguments?.getBoolean("isNew")!!
            val orderID = it.arguments?.getInt("orderId")
            OrderTracking(navController = navController, cViewModel = viewModel, isNew = isNew, orderID = orderID)
        }
        composable(route = Screen.Category.route,
            deepLinks = listOf(navDeepLink {
                uriPattern = "target://weiredurl.com/{tags}"
                action = Intent.ACTION_VIEW
            }),
            arguments = listOf(
                navArgument("bannerId"){
                    defaultValue = -1
                    type = NavType.IntType
                },
                navArgument("catId"){
                    defaultValue = -1
                    type = NavType.IntType
                }
            )

        ){
            val tag = it.arguments?.getInt("bannerId") ?: -1
            val catId = it.arguments?.getInt("catId")
//            Category(navController = navController, viewModel = viewModel, tags =if (tags==-1) null else  tags, catId = if (catId==-1) null else catId)
            NewCategory(navController = navController, id = tag, mainViewModel = viewModel)
        }
        composable(route = Screen.Product.route,
            arguments = listOf(
                navArgument("id"){
                    defaultValue = -1
                    type = NavType.IntType
                }
            )
        ){
            val id = it.arguments?.getInt("id") ?: -1
            ProductScreen(navController = navController, id = id)
        }
    }
}