package com.target.supermarket.presentation.loaders

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.target.supermarket.presentation.interfacess.BorderOrder
import com.target.supermarket.presentation.interfacess.drawSegmentedBorder
import com.target.supermarket.presentation.navigation.Screen
import com.target.supermarket.presentation.viewModels.CommonContract
import com.target.supermarket.utilities.CommonMethods
import com.target.supermarket.utilities.Loader
import com.valentinilk.shimmer.Shimmer

@Composable
fun InterfaceThreeUnScrollLoader(modifier: Modifier = Modifier, shimmer: Shimmer) {
    val width = LocalConfiguration.current.screenWidthDp
    val rem = width / 2
    val calW = if (rem > 250) (width/3.1).toInt()  else (width/2.1).toInt()
    val height = calW * 1.2
    Column(modifier = modifier
        .fillMaxSize()
        .padding(8.dp)){
        val products = listOf(1,2)
        for (i in products.chunked(if (rem>250) 3 else 2)){
            Row {
                for (p in i){
                    val order = if(p != i.last() || (products.size%2 != 0 && p==products.last())) BorderOrder.End else BorderOrder.Start
                    Box(modifier = Modifier
                        .width(calW.dp)
                        .height(height.dp)
                        .drawSegmentedBorder(
                            strokeWidth = 1.dp,
                            color = Color.LightGray,
                            borderOrder = order,
                            cornerPercent = 40,
                            drawDivider = !i.contains(products.last())
                        ), contentAlignment = Alignment.Center) {
                        Column(modifier = Modifier) {
                            Card(modifier = Modifier
                                .fillMaxWidth()
                                .weight(10f)
                                .padding(10.dp), shape = RoundedCornerShape(topStart = 16.dp, bottomEnd = 16.dp, topEnd = 14.dp, bottomStart = 14.dp),
                                elevation = 0.dp
                            ) {
                                Box {
                                    Loader(shimmer = shimmer, isLoading = true)
                                }
                            }
                            Box(modifier = Modifier
                                .weight(3f)
                                .padding(horizontal = 16.dp), contentAlignment = Alignment.Center) {
                                TextLoader(shimmer = shimmer, text = "...............")
                            }
                        }
                    }
                }
            }
        }
    }
}