package com.target.targetapp.presentation.home.deals

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun DealsTimer(modifier: Modifier = Modifier, bigHeader:String, description:String) {
    Column(modifier = modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = bigHeader, style = MaterialTheme.typography.h5)
        Text(text = description, style = MaterialTheme.typography.caption)
//        Row(modifier = Modifier.wrapContentSize().padding(vertical = 16.dp), verticalAlignment = Alignment.CenterVertically) {
//            TimerContainer(text = "00")
//            TimerColons()
//            TimerContainer(text = "00")
//            TimerColons()
//            TimerContainer(text = "00")
//            TimerColons()
//            TimerContainer(text = "00")
//        }
    }
}

@Composable
fun TimerContainer(modifier:Modifier = Modifier.size(50.dp), text:String) {
    Box(modifier = modifier.background(color = Color.Green, shape = RoundedCornerShape(10)), contentAlignment = Alignment.Center) {
        Text(text = text, style = MaterialTheme.typography.h5.copy(color = Color.DarkGray))
    }
}

@Composable
fun TimerColons(modifier: Modifier = Modifier.padding(horizontal = 8.dp)) {
    Text(text = ":", style = MaterialTheme.typography.h5, modifier = modifier)
}

@Preview(showBackground = true)
@Composable
fun PreviewDealsTimer() {
    DealsTimer(bigHeader = "Best Deals Of This Week", description = "A virtual Assistant collects the products from your list")
}