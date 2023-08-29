package com.target.supermarket.presentation.loaders

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.target.supermarket.utilities.Loader
import com.valentinilk.shimmer.Shimmer

@Composable
fun RecommendedLoader(modifier: Modifier = Modifier, shimmer: Shimmer) {
    LazyRow(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)){
        items(5){
            LoaderItem(shimmer = shimmer, modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp))
        }
    }
}

@Composable
fun LoaderItem(modifier: Modifier = Modifier, shimmer: Shimmer) {
    Column(modifier = modifier) {
        Surface(shape = CircleShape, modifier = Modifier.size(120.dp)) {
            Box(modifier = Modifier
                .fillMaxSize()) {
                Loader(shimmer = shimmer, isLoading = true)
            }
        }
    }
}