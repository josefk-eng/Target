package com.target.supermarket.presentation.commons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun TextInputWidget(modifier: Modifier = Modifier, value:String, label:String, onValueChange:(String)->Unit) {
    Column(modifier = modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp)) {
//        Text(text = label, style = MaterialTheme.typography.caption.copy(fontWeight = FontWeight.ExtraBold))
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = modifier
                .wrapContentWidth()
                .background(
                    color = Color.LightGray.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(16.dp)
                )
                .height(45.dp)
                .padding(vertical = 8.dp, horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            FormInput(modifier = Modifier.wrapContentWidth(), value = value, label = label, onValueChange = {
                onValueChange(it)
            })
        }
    }
}