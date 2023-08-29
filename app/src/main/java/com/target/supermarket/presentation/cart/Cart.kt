package com.target.supermarket.presentation.cart

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.target.supermarket.domain.models.LocalProduct
import com.target.supermarket.presentation.commons.PagerTopBar
import com.target.supermarket.presentation.navigation.Screen
import com.target.supermarket.presentation.viewModels.CommonContract
import com.target.supermarket.presentation.viewModels.CommonViewModel
import com.target.supermarket.utilities.CommonMethods

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Cart(navController:NavHostController, viewModel:CommonViewModel) {
    val pagerState = rememberPagerState(initialPage = 0)
    val scope = rememberCoroutineScope()
    LaunchedEffect(key1 = viewModel.state.value.cartItems){
        if (viewModel.state.value.cartItems.isEmpty()){
            CommonMethods.pop(navController)
        }
    }
    Scaffold(
        modifier = Modifier,
        topBar = {
            PagerTopBar(
                navController = navController,
                scope = scope,
                pagerState = pagerState,
                viewModel = viewModel
            )
        },
        backgroundColor = Color.Transparent
    ) {
        HorizontalPager(state = pagerState, modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(it), count = 2) {position ->
            when(position){
                0 -> CartList(onCheck = {
                    navController.navigate(Screen.CheckOut.route)
                    viewModel.setEvent(CommonContract.CommonEvent.OnCheckOut)
                                        }, viewModel = viewModel)
                1 -> SavedForLater(viewModel = viewModel)
            }
        }
    }
}