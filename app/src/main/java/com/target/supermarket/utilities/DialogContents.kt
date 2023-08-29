package com.target.supermarket.utilities

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.target.supermarket.R
import com.target.supermarket.ui.theme.errorColor
import com.target.supermarket.ui.theme.lightBlue

@Composable
fun NetworkError(error:String, onContinue:()->Unit) {
    val width = LocalConfiguration.current.screenWidthDp
    Card() {
        Box(modifier = Modifier
            .size((width*0.8).dp)
            .padding(16.dp)) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(bottom = 32.dp).align(
                Alignment.Center)) {
                Icon(painter = painterResource(id = R.drawable.offline), contentDescription = "", tint = errorColor, modifier = Modifier.size(100.dp))
                Text(text = error, style = MaterialTheme.typography.h6.copy(color = errorColor, fontSize = 18.sp, textAlign = TextAlign.Center), maxLines = 2, overflow = TextOverflow.Ellipsis)
            }
            Row(modifier = Modifier.align(Alignment.BottomEnd)) {
                TextButton(onClick = { /*TODO*/ }) {
                    Text(text = "Settings")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { onContinue() }) {
                    Text(text = "Continue")
                }
            }
        }
    }
}