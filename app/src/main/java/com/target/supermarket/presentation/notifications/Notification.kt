package com.target.supermarket.presentation.notifications

import android.graphics.Paint.Align
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.target.supermarket.R
import com.target.supermarket.domain.models.TargetNotification
import com.target.supermarket.domain.usecases.main.NotificationMenuClick
import com.target.supermarket.presentation.commons.getImage
import com.target.supermarket.presentation.navigation.BottomScreenItem
import com.target.supermarket.presentation.viewModels.CommonContract
import com.target.supermarket.presentation.viewModels.CommonViewModel
import com.target.supermarket.ui.theme.lightBack
import com.target.supermarket.ui.theme.lightBlue
import com.target.supermarket.utilities.CommonMethods

@Composable
fun Notification(modifier: Modifier = Modifier, viewModel:CommonViewModel, controller:NavHostController) {
    val dimex = LocalConfiguration.current.screenWidthDp
    val dimey = LocalConfiguration.current.screenHeightDp
    Box(modifier = modifier.fillMaxSize()){
        Card(shape = CircleShape, modifier = Modifier
            .align(Alignment.TopEnd)
            .padding(top = 16.dp, end = 16.dp)) {
            IconButton(onClick = { viewModel.setEvent(CommonContract.CommonEvent.OpenNotificationDialog(false){}) }) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "")
            }
        }
        Card(shape = RoundedCornerShape(16.dp), modifier = Modifier
            .align(Alignment.Center)
            .size((dimex * 0.9).dp, (dimey * 0.8).dp)) {
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Notifications", style = MaterialTheme.typography.h5, modifier = Modifier.padding(vertical = 8.dp))
                Divider()
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Sort By: ")
                    Spacer(modifier = Modifier.width(8.dp))
                    SortButton(selected = true, label = "UnRead")
                    Spacer(modifier = Modifier.width(8.dp))
                    SortButton(selected = false, label = "Date")
                }
                Divider()
                if (viewModel.state.value.notifications.isEmpty()){
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(painter = painterResource(id = R.drawable.not), contentDescription = "", tint = lightBlue, modifier = Modifier
                                .size(60.dp)
                                .padding(bottom = 8.dp))
                            Text(text = "Notifications will appear here", style = MaterialTheme.typography.h6)
                        }
                    }
                }else{
                    LazyColumn{
                        items(viewModel.state.value.notifications){notification->
                            NotificationItem(notification = notification, onMenuClick = {
                                viewModel.setEvent(CommonContract.CommonEvent.NotificationMenuClick(it,notification))
                            }){
                                if (notification.title.contains("reference", ignoreCase = true)){
                                    viewModel.setEvent(CommonContract.CommonEvent.OpenNotificationDialog(false){})
                                    viewModel.setEvent(CommonContract.CommonEvent.AutoNavigateToOrder)
                                    CommonMethods.navigate(controller,
                                        BottomScreenItem.Home.passArg(to = true, orderId = notification.target.toInt()))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SortButton(modifier: Modifier = Modifier, selected:Boolean, label:String) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        RadioButton(selected = selected, onClick = { /*TODO*/ })
        Text(text = label)
    }
}

@Composable
fun NotificationItem(modifier: Modifier = Modifier, notification:TargetNotification, onMenuClick:(String)->Unit, onClickToNavigate:()->Unit) {
    val image = getImage(url = notification.image, onSuccess = {}) {

    }
    Column(modifier = modifier
        .fillMaxWidth()
        .padding(8.dp)
        .clickable { onClickToNavigate() }
        .background(
            shape = RoundedCornerShape(16.dp),
            color = if (notification.read) Color.Transparent else lightBlue.copy(alpha = 0.3f)
        )) {
        Row(modifier = Modifier
            .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier
                .fillMaxHeight().width(75.dp).background(color = Color.White)) {
                Image(
                    painter = image,
                    contentDescription = "",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillBounds
                )
            }
            Column(modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(start = 8.dp, bottom = 8.dp)) {
                Text(text = notification.title, style = MaterialTheme.typography.h6.copy(fontSize = 14.sp), maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(text = notification.description, style = MaterialTheme.typography.caption, maxLines = 2, overflow = TextOverflow.Ellipsis)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = notification.date, style = MaterialTheme.typography.caption)
            }
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                var expandMenu by rememberSaveable{ mutableStateOf(false) }
                val menuItems = listOf("Mark as read","Delete")
                DropdownMenu(expanded = expandMenu, onDismissRequest = { expandMenu = !expandMenu }) {
                    for (m in menuItems){
                        if (!(notification.read && m.equals("Mark as read", ignoreCase = true))){
                            DropdownMenuItem(onClick = {
                                onMenuClick(m)
                                expandMenu = false
                            }) {
                                Text(text =m)
                            }
                        }
                    }
                }
                IconButton(onClick = { expandMenu = true }) {
                    Icon(imageVector = Icons.Default.MoreVert, contentDescription = "", modifier = Modifier.size(60.dp))
                }
            }
        }
    }
}