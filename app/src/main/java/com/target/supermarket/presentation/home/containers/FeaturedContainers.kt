package com.target.supermarket.presentation.home.containers

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.target.supermarket.domain.models.Banner
import com.target.supermarket.presentation.loaders.FeaturedCategoryLoader
import com.target.supermarket.presentation.loaders.RecommendedLoader
import com.target.supermarket.presentation.navigation.Screen
import com.target.supermarket.presentation.viewModels.CommonContract
import com.target.supermarket.presentation.viewModels.CommonViewModel
import com.target.supermarket.presentation.viewModels.HomeViewModel
import com.target.supermarket.utilities.CommonMethods
import com.target.supermarket.utilities.Items
import com.target.supermarket.utilities.ItemsLoadingStatus
import com.target.targetapp.presentation.home.containers.SemiFeaturedCategory
import com.valentinilk.shimmer.Shimmer
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun FeaturedContainers(modifier: Modifier = Modifier,shimmer: Shimmer, viewModel: HomeViewModel, topNaHost:NavHostController) {
    val imageLoading = rememberSaveable{ mutableStateOf(true) }
    Box(modifier = modifier
        .fillMaxWidth()
        .height(310.dp))
    {
        FeaturedCategory(viewModel = viewModel, shimmer = shimmer, imageLoading = imageLoading){
            topNaHost.navigate(Screen.Category.passArg(bannerId = it))
        }
//        Spacer(modifier = Modifier.height(5.dp))
//            SemiFeat(viewModel = viewModel, shimmer = shimmer, onClick = {b->
//                navController.navigate(Screen.Category.route)
//                navController.navigate(Screen.Category.passArg(
//                    tags = b.tags,
//                    banner = b.image
//                ))
//            })


//        Spacer(modifier = Modifier.height(16.dp))
//        Text(text = "RECOMMENDED FOR YOU: ", style = MaterialTheme.typography.h6.copy(fontSize = 14.sp), modifier = Modifier.padding(start = 8.dp, bottom = 8.dp))

//        if (){
//            RecommendedLoader(shimmer = shimmer)
//        }else {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(start = 8.dp, end = 8.dp)
            ) {
                items(viewModel.state.value.recommended) { cat ->
                    CircledCategory(
                        modifier = Modifier,
                        product = cat,
                        shimmer = shimmer,
                        viewModel = viewModel,
                        isLoading = viewModel.state.value.loaders[Items.Recommended] == ItemsLoadingStatus.Loading
                    ) {
                        CommonMethods.navigate(navController = topNaHost, Screen.Product.passArg(cat.id))
                    }
                }
//            }
        }
    }
}
