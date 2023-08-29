package com.target.supermarket.presentation.ordertracker

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.target.supermarket.R
import com.target.supermarket.ui.theme.lightBack
import com.target.supermarket.ui.theme.lightBlue
import com.target.supermarket.ui.theme.lighterGray

@Composable
fun TrackItem(modifier: Modifier = Modifier, isActive:Boolean, itemHeader:String, itemDescription:String, time:String, resId:Int) {
    val style = MaterialTheme.typography.caption.copy(color = if (isActive) lightBack else Color.LightGray)
    Column(modifier = modifier.padding(horizontal = 10.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()) {
            Box(modifier = Modifier
                .size(16.dp)
                .background(
                    shape = CircleShape,
                    color = if (isActive) lightBlue else Color.LightGray
                ))
            Icon(painter = painterResource(id = resId), contentDescription = "", tint = if (isActive) Color.DarkGray else lightBack, modifier = Modifier
                .padding(horizontal = 8.dp)
                .size(40.dp))
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Column() {
                    Text(text = itemHeader, style = MaterialTheme.typography.caption.copy(fontSize = 18.sp, color = if (isActive) Color.Black else Color.LightGray))
                    Text(text = itemDescription, style = style)
                }
                Text(text = time, style = style)
            }
        }
        if (!itemHeader.contains("completed", ignoreCase = true))
            Column(modifier = Modifier.width(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                for (i in 1..3){
                    SmallDots()
                }
            }
    }
}
@Preview(showBackground = true)
@Composable
fun TrackPreview() {
    TrackItem(isActive = false, itemHeader = "Ready To Pick Up", itemDescription = "Order 567378 from Target", time = "11:00", resId = R.drawable.visibility)
}

@Composable
fun SmallDots(modifier: Modifier = Modifier) {
    Surface(modifier = modifier
        .padding(vertical = 4.dp)) {
        Box(modifier = Modifier
            .size(10.dp)
            .background(shape = CircleShape, color = Color.Green))
    }
}