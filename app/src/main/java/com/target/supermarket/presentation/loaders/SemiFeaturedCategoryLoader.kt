package com.target.supermarket.presentation.loaders

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.target.supermarket.utilities.Loader
import com.valentinilk.shimmer.Shimmer

@Composable
fun SemiFeaturedCategoryLoader(modifier: Modifier = Modifier, shimmer:Shimmer) {
    Surface(
        modifier = modifier
            .height(150.dp),
        color = Color.Transparent

    ) {
        Box(Modifier.fillMaxSize()) {
            Loader(shimmer = shimmer, isLoading = true)
        }
    }
}