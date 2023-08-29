package com.target.supermarket.presentation.launcher

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.target.supermarket.presentation.navigation.Screen
import kotlinx.coroutines.flow.collectLatest
import com.target.supermarket.R
import com.target.supermarket.utilities.NetworkError
import com.target.supermarket.presentation.checkout.CustomDialog
import com.target.supermarket.presentation.commons.ProgressIndicatorLoading
import com.target.supermarket.presentation.navigation.BottomScreenItem
import com.target.supermarket.presentation.viewModels.LauncherContract
import com.target.supermarket.presentation.viewModels.LauncherViewModel
import com.target.supermarket.utilities.CommonMethods
import com.target.supermarket.ui.theme.lightBlue

@Composable
fun LauncherScreen(modifier: Modifier=Modifier.fillMaxSize(), navController: NavHostController) {
    val viewModel: LauncherViewModel = hiltViewModel()
    var errorSignal by rememberSaveable{ mutableStateOf(false) }
    LaunchedEffect(key1 = Unit){
        viewModel.effect.collectLatest {
            when(it){
                is LauncherContract.LauncherEffect.OnLoadingDone -> {
                    if (it.exception==null){
                        viewModel.setEvent(LauncherContract.LauncherEvent.MoveOnRegardless)
                    }else {
                        viewModel.setEvent(LauncherContract.LauncherEvent.OnExceptionChanged(it.exception))
                        errorSignal = true
                    }
                }
                LauncherContract.LauncherEffect.MoveOnRegardless -> {
                    CommonMethods.navigate(navController,
                        if (viewModel.state.value.onFirstLaunch) Screen.OnBord.route else BottomScreenItem.Home.passArg(false, orderId = -1), Screen.Launcher.route)
                }
            }
        }
    }
    Scaffold(modifier = modifier, backgroundColor = Color.Transparent) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(it)) {
            Column(
                Modifier
                    .wrapContentSize()
                    .align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                Image(painter = painterResource(id = R.drawable.instantshop), contentDescription = "", Modifier.size(350.dp))
            }
            Column(modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Target Supermarket", style = MaterialTheme.typography.h6.copy(color = lightBlue))
                Text(text = "\u00A9 2023 All Rights Reserved", style = MaterialTheme.typography.caption.copy(fontStyle = FontStyle.Italic))
            }


            if (errorSignal){
                CustomDialog(onNegativeClick = {
                    errorSignal = false
                }) {
                    Log.e("REMOTE ERROR", "LauncherScreen: ${viewModel.state.value.remoteException}", )
                    NetworkError(error = viewModel.state.value.remoteException?.stackTrace?.get(0)?.lineNumber?.toString() ?: "You Are Not Connected To The Internet") {
                        errorSignal = false
                        viewModel.setEvent(LauncherContract.LauncherEvent.MoveOnRegardless)
                    }
                }
            }
        }
    }
}