package com.target.supermarket.presentation.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.target.supermarket.R
import com.target.supermarket.presentation.navigation.BottomScreenItem
import com.target.supermarket.presentation.viewModels.CommonContract
import com.target.supermarket.presentation.viewModels.CommonViewModel
import com.target.supermarket.presentation.viewModels.OnBoardingContract
import com.target.supermarket.presentation.viewModels.OnBoardingViewModel
import com.target.supermarket.ui.theme.lightBlue
import com.target.supermarket.utilities.CommonMethods
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnBoardingScreen(modifier: Modifier = Modifier.fillMaxSize(), navController: NavHostController) {
    val state = rememberPagerState()
    val scope = rememberCoroutineScope()
    val viewModel:OnBoardingViewModel = hiltViewModel()
    Box(modifier = modifier){
        HorizontalPager(state = state, count = 4) {position->
            when(position){
                0-> Frame(screen = Screen(res = R.drawable.shopping_made_easier, title = "Shopping has just got better"))
                1-> Frame(screen = Screen(res = R.drawable.simplified_selection, title = "Enhanced Production Selection"))
                2-> Frame(screen = Screen(res = R.drawable.cart_made_easier, title = "Save For Later"))
                3-> Frame(screen = Screen(res = R.drawable.right_up_to_your_door, title = "Instant Delivery"))
            }
        }
        Row(
            Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .navigationBarsPadding()
                .align(Alignment.BottomCenter), horizontalArrangement = Arrangement.SpaceBetween) {
            if (state.currentPage!=0) {
                IconButton(onClick = { move(scope, state) }) {
                    Icon(painter = painterResource(id = R.drawable.back), contentDescription = "", tint = lightBlue, modifier = Modifier.size(30.dp))
                }
            }else{
                Spacer(modifier = Modifier.size(1.dp))
            }
            IconButton(onClick = {
                if (state.currentPage==3){
                    viewModel.setEvent(OnBoardingContract.OnBoardingEvent.MarkUser)
                    CommonMethods.navigate(navController,BottomScreenItem.Home.passArg(false, orderId = -1),
                        com.target.supermarket.presentation.navigation.Screen.OnBord.route)
                }else {
                move(scope,state, isForward = true) }
            }) {
                Icon(painter = painterResource(id = R.drawable.forward), contentDescription = "", tint = lightBlue, modifier = Modifier.size(30.dp))
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
fun move(scope: CoroutineScope, state: PagerState, isForward:Boolean = false){
    scope.launch {
        state.animateScrollToPage(if (isForward) state.currentPage+1 else state.currentPage-1)
    }
}

