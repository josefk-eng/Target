package com.target.supermarket.presentation.commons

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.target.supermarket.presentation.viewModels.CommonViewModel
import com.target.supermarket.presentation.viewModels.DetailedListViewModel
import com.target.supermarket.ui.theme.lighterGray
import com.target.supermarket.utilities.CommonMethods
import com.target.supermarket.utilities.Loader
import com.valentinilk.shimmer.Shimmer
import me.onebone.toolbar.*

@Composable
fun CollapsibleScreen(
    navController: NavHostController,
    viewModel: CommonViewModel,
    shimmer:Shimmer,
    content: @Composable ()->Unit) {
    val state = rememberCollapsingToolbarScaffoldState()
    val textSize = (18 + (30 - 18) * state.toolbarState.progress).sp
    val imageSize = (140 + (200 - 140) * state.toolbarState.progress)
    val image = getImage(url = viewModel.state.value.category?.first?.image ?: "", onSuccess = {}) {
    }
    CollapsingToolbarScaffold(
        modifier = Modifier.fillMaxSize(),
        state = state,
        scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
        toolbar = {
                Card(modifier = Modifier
                    .fillMaxWidth()
                    .height(imageSize.dp)
                    .parallax(0.5f), shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp),
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ){
                        if (viewModel.state.value.category != null)
                            Image(painter = image,
                                contentDescription = "",
                                contentScale = ContentScale.FillBounds,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .graphicsLayer {
                                        // change alpha of Image as the toolbar expands
                                        alpha = state.toolbarState.progress
                                    })

                        Loader(shimmer = shimmer, isLoading = viewModel.state.value.category == null )
                    }
                }

            Text(
                text = (viewModel.state.value.category?.first?.name ?: "").ifEmpty { "....." },
                modifier = Modifier
                    .road(Alignment.CenterStart, Alignment.BottomEnd)
                    .padding(60.dp, 16.dp, 60.dp, 16.dp),
                style = MaterialTheme.typography.h5.copy(color = if(textSize>18.sp) Color.White else Color.LightGray, fontWeight = FontWeight.Bold, fontSize = textSize),
            )

            SearchableAppBar(
                modifier = Modifier.pin(),
                navController = navController,
                leading = Icons.Default.ArrowBack,
                viewModel = viewModel, ctrSearch = true){ CommonMethods.pop(navController)}
        }
    ){
        content()
    }
}