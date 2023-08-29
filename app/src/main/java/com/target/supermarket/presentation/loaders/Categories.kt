package com.target.supermarket.presentation.loaders

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.target.supermarket.utilities.Loader
import com.valentinilk.shimmer.Shimmer

@Composable
fun Categories(modifier: Modifier = Modifier, shimmer: Shimmer) {
    LazyRow(modifier = modifier.padding(horizontal = 12.dp)){
        items(5){
            CatItem(shimmer = shimmer, modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp))
        }
    }
}


@Composable
fun CatItem(modifier: Modifier = Modifier, shimmer: Shimmer) {
    Surface(shape = RoundedCornerShape(40.dp), modifier = modifier){
        Box(modifier = Modifier.width(100.dp).height(40.dp)){
            Loader(shimmer = shimmer, isLoading = true)
        }
    }
}