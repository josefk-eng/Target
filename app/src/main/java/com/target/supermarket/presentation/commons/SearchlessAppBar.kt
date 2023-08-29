package com.target.supermarket.presentation.commons

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.target.supermarket.ui.theme.topAppBar

@Composable
fun SearchlessBar(modifier: Modifier = Modifier.fillMaxWidth(), title:String, leading:ImageVector, leadingClick:()->Unit) {
    TopAppBar(backgroundColor = topAppBar, elevation = 0.dp, modifier = modifier.statusBarsPadding()) {
        IconButton(onClick = leadingClick) {
            Icon(imageVector = leading, contentDescription = "")
        }
        Text(text = title, style = MaterialTheme.typography.h6,modifier = Modifier.weight(10f))
    }
}