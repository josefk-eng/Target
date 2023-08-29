package com.target.supermarket.presentation.checkout

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.target.supermarket.R
import com.target.supermarket.presentation.cart.TextButtonCustom
import com.target.supermarket.presentation.commons.RecieptDialog
import com.target.supermarket.presentation.navigation.Screen
import com.target.supermarket.presentation.viewModels.CommonContract
import com.target.supermarket.presentation.viewModels.CommonViewModel
import com.target.supermarket.ui.theme.baseColor
import com.target.supermarket.ui.theme.lightBlue
import com.target.supermarket.utilities.CommonMethods

@Composable
fun ThanksScreen(navController:NavHostController, viewModel:CommonViewModel) {
    val showReceipt = remember{ mutableStateOf(false) }
    Scaffold(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize().padding(it)) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.align(
                Alignment.Center).padding(horizontal = 8.dp)) {
                Icon(painter = painterResource(id = R.drawable.success), contentDescription = "", tint = Color.Green, modifier = Modifier.size(80.dp))
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Order Has Been Received Thank You!", style = MaterialTheme.typography.h6.copy(color = lightBlue, fontSize = 15.sp))
                Spacer(modifier = Modifier.height(32.dp))
                Text(text = "For Inquiries Please Call On 000000000", style = MaterialTheme.typography.caption.copy(fontStyle = FontStyle.Italic, fontWeight = FontWeight.ExtraBold, color = baseColor))
            }
            TextButtonCustom(title = "Get Payment Receipt", modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 32.dp)
                .align(
                    Alignment.BottomCenter
                )) {
//                CommonMethods.navigate(navController,Screen.Home.route,Screen.Final.route)
                showReceipt.value = true
                viewModel.setEvent(CommonContract.CommonEvent.FinalCleanUp)
            }

            if (showReceipt.value)
                RecieptDialog(onDismiss = {
                    showReceipt.value = false
                    CommonMethods.navigate(navController,Screen.Home.route,Screen.Final.route)
                }){
                    showReceipt.value = false
                    CommonMethods.navigate(navController,Screen.Home.route,Screen.Final.route)
                }
        }
    }
}