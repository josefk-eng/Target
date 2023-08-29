package com.target.supermarket.presentation.commons

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.target.supermarket.ui.theme.lightBlue

@Composable
fun LoadingOverlay(isLoading:State<Boolean>) {
    val fraction = remember { Animatable(0f) }
    var reveal by remember{ mutableStateOf(false) }
    LaunchedEffect(Unit){
        while (isLoading.value){
            fraction.animateTo(1f, tween(2000))
            fraction.snapTo(0f)
        }
        reveal = true
        fraction.animateTo(1f, tween(2000))
    }

    if (!reveal){
        //opaque cover under gradient bar
        Box(modifier = Modifier.fillMaxWidth().background(color = lightBlue))
    }
}

@Preview
@Composable
fun PrevLoader() {
    val st = remember { mutableStateOf(true) }
    LoadingOverlay(isLoading = st)
}