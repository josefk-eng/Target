package com.target.supermarket.presentation.loaders

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.target.supermarket.utilities.Loader
import com.valentinilk.shimmer.Shimmer
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer

@Composable
fun TextLoader(modifier: Modifier = Modifier, shimmer: Shimmer, text:String) {
    Card(shape = RoundedCornerShape(8.dp), elevation = 0.dp) {
        Box(modifier = modifier
            .height(IntrinsicSize.Min)
            .width(IntrinsicSize.Min)) {
            Text(text = text, modifier = Modifier.padding(horizontal = 16.dp).alpha(0f))
            Loader(shimmer = shimmer, isLoading = true)
        }
    }
}
