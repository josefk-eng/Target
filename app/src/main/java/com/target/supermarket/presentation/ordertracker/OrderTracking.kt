package com.target.supermarket.presentation.ordertracker

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.target.supermarket.R
import com.target.supermarket.presentation.commons.RecieptDialog
import com.target.supermarket.presentation.viewModels.CommonContract
import com.target.supermarket.presentation.viewModels.CommonViewModel
import com.target.supermarket.ui.theme.baseColor
import com.target.supermarket.ui.theme.lightBlue

@Composable
fun OrderTracking(modifier: Modifier = Modifier.fillMaxSize(), navController:NavHostController, cViewModel:CommonViewModel, isNew:Boolean, orderID:Int? = null) {
    var orderId = orderID
    LaunchedEffect(key1 = Unit){

    }
    Column(modifier = modifier) {
        Text(text = "My Orders", style = MaterialTheme.typography.h5.copy(textAlign = TextAlign.Center), modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp))
        Box(modifier = Modifier.fillMaxSize()){
            if (cViewModel.state.value.orders.isEmpty()){
                EmptyOrders(modifier = Modifier
                    .align(Alignment.Center)
                    .padding(16.dp))
            }else{
                LazyColumn(modifier = Modifier
                    .fillMaxSize()
                    .padding(14.dp))
                {
                    items(cViewModel.state.value.orders.sortedByDescending { it.id }){order->
                        var expandOrder by rememberSaveable{ mutableStateOf(false) }
                        val headerStyle = MaterialTheme.typography.caption.copy(color = Color.DarkGray, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
                        Card(modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                            .wrapContentHeight(), elevation = 4.dp) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(modifier = Modifier
                                    .fillMaxWidth(),verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                                    Column {
                                        Row {
                                            Text(text = "Order", style = headerStyle)
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(text = "${order.id}", style = headerStyle.copy(color = lightBlue))
                                        }
                                        Row {
                                            Text(text = "Amount: ", style = MaterialTheme.typography.caption.copy(color = Color.DarkGray, fontSize = 16.sp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(text = "${order.price}", style = MaterialTheme.typography.caption.copy(color = lightBlue, fontSize = 16.sp))
                                        }
                                        Text(text = "${order.status} on ${order.date_added}", style = MaterialTheme.typography.caption)

                                    }
                                    IconButton(onClick = { expandOrder = !expandOrder }) {
                                        Icon(painter = painterResource(id = if (expandOrder) R.drawable.up else R.drawable.down), contentDescription = "", modifier = Modifier.size(35.dp), tint = baseColor)
                                    }
                                }
                                AnimatedVisibility(visible = (expandOrder || order.id == orderId)) {
                                    orderId = null
                                    Column(modifier = Modifier.padding(vertical = 25.dp)){
                                        TrackItem(isActive = true, itemHeader = "Order Placed", itemDescription = "Your order payment has been confirmed", time = "11:00", resId = com.target.supermarket.R.drawable.placed)
                                        TrackItem(isActive = false, itemHeader = "Order Processed", itemDescription = "Your order is ready for pickup", time = "11:00", resId = com.target.supermarket.R.drawable.processed)
                                        TrackItem(isActive = false, itemHeader = "In Delivery", itemDescription = "Your order is on the way", time = "11:00", resId = com.target.supermarket.R.drawable.indelivery)
                                        TrackItem(isActive = false, itemHeader = "Delivered", itemDescription = "Your order has been delivered", time = "11:00", resId = com.target.supermarket.R.drawable.delivered)
                                        TrackItem(isActive = false, itemHeader = "Completed", itemDescription = "Order Id T20000 has been completed", time = "11:00", resId = com.target.supermarket.R.drawable.completed)
                                    }
                                }
                            }
                        }
                    }
                }
                if (cViewModel.state.value.to)
                    RecieptDialog(onDismiss = {
                        cViewModel.setEvent(CommonContract.CommonEvent.AutoNavigateToOrder)
                    }){cViewModel.setEvent(CommonContract.CommonEvent.AutoNavigateToOrder)}
            }
        }
    }
}

@Composable
fun EmptyOrders(modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(painter = painterResource(id = R.drawable.note), contentDescription = "", tint = baseColor, modifier = Modifier.size(100.dp))
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Make some orders and they will appear here for tracking", style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.ExtraBold, textAlign = TextAlign.Center), modifier = Modifier.padding(horizontal = 16.dp))
    }
}