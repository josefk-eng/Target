package com.target.supermarket.presentation.payments

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.target.supermarket.R
import com.target.supermarket.ui.theme.lightBlue

@Composable
fun PaymentTab(modifier: Modifier, isActive:Boolean, tabText:String, resId:Int) {
    Card(modifier = modifier
        .width(100.dp)
        .height(120.dp), backgroundColor = if (isActive) lightBlue else Color.LightGray) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(modifier = Modifier
                .size(50.dp)
                .background(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.LightGray.copy(alpha = 0.3f)
                )) {
                Image(modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp) ,painter = painterResource(id = resId), contentDescription = "", contentScale = ContentScale.FillBounds)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = tabText, style = MaterialTheme.typography.caption.copy(color = if (isActive) Color.White else lightBlue, fontSize = 10.sp ))
        }
    }
}

@Composable
fun PaymentTab1(modifier: Modifier, isActive:Boolean, tabText:String, resId:Int) {
    val width = LocalConfiguration.current.screenWidthDp
    Card(modifier = modifier
        .width((width * 0.3).dp)
        .height((width * 0.1).dp), backgroundColor = if (isActive) lightBlue else Color.Transparent, elevation = 0.dp) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        shape = RoundedCornerShape(16.dp),
                        color = Color.Transparent
                    )
            ) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(id = resId),
                    contentDescription = "",
                    contentScale = ContentScale.FillBounds
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = tabText, style = MaterialTheme.typography.caption.copy(color = if (isActive) Color.White else lightBlue, fontSize = 10.sp ))
        }
    }
}



@Preview(showBackground = true)
@Composable
fun Prev() {
//    PaymentTab(modifier = Modifier.padding(8.dp), isActive = false, tabText = "Airtel Money")
}