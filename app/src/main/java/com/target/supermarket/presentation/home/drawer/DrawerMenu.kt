package com.target.targetapp.presentation.home.drawer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.target.supermarket.R
import com.target.supermarket.presentation.checkout.CustomDialog
import com.target.supermarket.presentation.navigation.Screen
import com.target.supermarket.ui.theme.lightBack
import com.target.supermarket.ui.theme.lightBlue
import com.target.supermarket.ui.theme.lighterGray
import com.target.supermarket.utilities.CommonMethods
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun DrawerMenu(modifier: Modifier, navController:NavHostController, drawerState: DrawerState, scope:CoroutineScope) {
    var versionOpen by rememberSaveable{ mutableStateOf(false) }
    Box(modifier = modifier) {
        Column(
            Modifier
                .wrapContentSize()
                .padding(horizontal = 16.dp)
                .align(Alignment.TopCenter)) {
            MenuItem(text = "Home"){
                scope.launch { drawerState.close() }
            }
            MenuItem(text = "History"){}
//            MenuItem(text = "Track Orders"){
//                CommonMethods.navigate(navController,Screen.Track.route)
//            }
            MenuItem(text = "About App"){
                scope.launch {
                    drawerState.close()
                    delay(1000)
                    versionOpen = true
                }
            }
        }
        Row(
            Modifier
                .wrapContentHeight()
                .padding(16.dp)
                .align(Alignment.BottomCenter),) {
            SocialItem(modifier = Modifier, id = R.drawable.twitter)
            SocialItem(modifier = Modifier, id = R.drawable.facebook)
            SocialItem(modifier = Modifier, id = R.drawable.pinterest)
            SocialItem(modifier = Modifier, id = R.drawable.instagram)
            SocialItem(modifier = Modifier, id = R.drawable.youtube)
        }

        if (versionOpen)
            CustomDialog(onNegativeClick = { versionOpen = false }) {
                Box {
                    IconButton(onClick = { versionOpen = false }, modifier = Modifier.align(Alignment.TopEnd)) {
                        Icon(painter = painterResource(id = R.drawable.close_circle), contentDescription = "", tint = lightBlue, modifier = Modifier.size(20.dp))
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(32.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.tlogo),
                            contentDescription = ""
                        )
                        Text(
                            text = "Target Easy Shopping",
                            style = MaterialTheme.typography.h5.copy(color = lightBlue)
                        )
                        Text(
                            text = "Version: 1.0.0 Alpha",
                            style = MaterialTheme.typography.h6.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = lightBack
                            )
                        )
                        Text(
                            text = "Designed by Jones & Estah Systems",
                            style = MaterialTheme.typography.h6.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = lightBack
                            )
                        )
                    }
                }
            }
    }
}

@Composable
fun MenuItem(modifier: Modifier = Modifier, text:String, onClick:()->Unit) {
    Column(modifier = modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .clickable { onClick() }) {
        Text(text = text, style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold))
        Divider(
            Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp, top = 8.dp))
    }
}

@Composable
fun SocialItem(modifier: Modifier, id:Int) {
    Box(modifier = modifier.padding(end = 8.dp)) {
        Box(
            modifier = Modifier
                .background(color = Color.LightGray, shape = RoundedCornerShape(4.dp))
                .size(35.dp), contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = id),
                contentDescription = "",
                Modifier.size(16.dp),
                tint = Color.DarkGray
            )
        }
    }
}

