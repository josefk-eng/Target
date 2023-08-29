package com.target.supermarket.presentation.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.target.supermarket.R

@Composable
fun Frame(modifier: Modifier = Modifier, screen: Screen) {
    val width = LocalConfiguration.current.screenWidthDp
    Box(modifier = modifier
        .fillMaxWidth()
        .fillMaxHeight(0.9f)
        .padding(8.dp)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.align(
            Alignment.Center)) {
            Image(painter = painterResource(id = screen.res), contentDescription = "", modifier = Modifier.size((width*0.5).dp))
            Spacer(modifier = Modifier.height(32.dp))
            Text(text = screen.title, style = MaterialTheme.typography.h5)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FramePreview() {
    Frame(screen = Screen(res = R.drawable.mobile, title = "Some Text"))
}