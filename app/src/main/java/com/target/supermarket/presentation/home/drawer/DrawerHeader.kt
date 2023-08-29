package com.target.supermarket.presentation.home.drawer

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.target.supermarket.R

@Composable
fun DrawerHeader(modifier: Modifier) {
    Box(contentAlignment = Alignment.Center, modifier = modifier) {
        Image(painter = painterResource(id = R.drawable.tlogo), contentDescription = "")
    }
}