package com.target.targetapp.presentation.home.containers

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.target.supermarket.presentation.commons.getImage
import com.target.supermarket.utilities.Loader
import com.valentinilk.shimmer.Shimmer

@Composable
fun SemiFeaturedCategory(modifier: Modifier = Modifier, shimmer: Shimmer, url:Any, height: Dp = 150.dp, onClick:(String)->Unit) {
    var loading by rememberSaveable{ mutableStateOf(true) }
    val feat = getImage(url = url, onSuccess = {
        loading = false
    }){
        loading = false
    }
    Surface(
        modifier = modifier
            .clickable { onClick("") }
            .height(height),
        color = Color.Transparent

    ) {
        Box(Modifier.fillMaxSize()) {
            Image(
                painter = feat,
                contentDescription = "",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )
            Loader(shimmer = shimmer, isLoading = loading)
        }
    }
}