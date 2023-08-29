package com.target.supermarket.presentation.loaders

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.target.supermarket.utilities.Loader
import com.valentinilk.shimmer.Shimmer

@Composable
fun FeaturedCategoryLoader(modifier: Modifier = Modifier, shimmer: Shimmer) {
    Surface(
        elevation = 4.dp,
        modifier = modifier
            .fillMaxWidth()
            .height(220.dp)
            .padding(bottom = 16.dp)
            .background(color = Color.LightGray, shape = RoundedCornerShape(20.dp))
    ) {
        Loader(shimmer = shimmer, isLoading = true)
    }
}