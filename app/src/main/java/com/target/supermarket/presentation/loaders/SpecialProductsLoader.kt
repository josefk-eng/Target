package com.target.supermarket.presentation.loaders

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.target.supermarket.presentation.home.SpecialProducts.TextTag
import com.target.supermarket.utilities.Loader
import com.target.supermarket.utilities.VerticalGrid
import com.valentinilk.shimmer.Shimmer

@Composable
fun SpecialProductsLoader(modifier: Modifier = Modifier, shimmer: Shimmer, isGrayed:Boolean = true) {
    Surface(color = Color.LightGray.copy(alpha = if (isGrayed) 0.2f else 0f), modifier = modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(vertical = 8.dp)) {
        Column(modifier = Modifier.padding(4.dp)) {
            TextLoader(shimmer = shimmer, text = "Short Text..")
            Spacer(modifier = Modifier.height(12.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                TextLoader(shimmer = shimmer, text = "Short Text........")
                TextLoader(shimmer = shimmer, text = "Short")
            }
            VerticalGrid(
                modifier = Modifier
                    .fillMaxWidth(),
            ){
                listOf(1,2).forEach {
                    CommonProductsLoader(shimmer = shimmer, modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp))
                }
            }
        }
    }
}


@Composable
fun CommonProductsLoader(modifier: Modifier = Modifier, shimmer: Shimmer) {
    Surface(modifier = modifier, shape = RoundedCornerShape(16.dp)) {
        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)) {
                Loader(shimmer = shimmer, isLoading = true)
            }
            Column(modifier = Modifier.fillMaxSize().padding(vertical = 4.dp, horizontal = 8.dp)) {
                TextLoader(shimmer = shimmer, text = "Short Text..")
                Spacer(modifier = Modifier.height(4.dp))
                TextLoader(shimmer = shimmer, text = "Short")
            }
        }
    }
}