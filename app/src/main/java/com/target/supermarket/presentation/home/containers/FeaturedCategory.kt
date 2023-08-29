package com.target.supermarket.presentation.home.containers

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import com.google.accompanist.pager.ExperimentalPagerApi
import com.target.supermarket.R
import com.target.supermarket.presentation.commons.getImage
import com.target.supermarket.presentation.loaders.FeaturedCategoryLoader
import com.target.supermarket.presentation.viewModels.CommonContract
import com.target.supermarket.presentation.viewModels.CommonViewModel
import com.target.supermarket.presentation.viewModels.HomeContract
import com.target.supermarket.presentation.viewModels.HomeViewModel
import com.target.supermarket.utilities.AutoSlidingCarousel
import com.target.supermarket.utilities.Items
import com.target.supermarket.utilities.ItemsLoadingStatus
import com.target.supermarket.utilities.Loader
import com.valentinilk.shimmer.Shimmer
import kotlinx.coroutines.delay

@OptIn(ExperimentalCoilApi::class, ExperimentalAnimationApi::class, ExperimentalPagerApi::class)
@Composable
fun FeaturedCategory(
    modifier: Modifier = Modifier.fillMaxWidth(),
    shimmer: Shimmer,
    viewModel: HomeViewModel,
    imageLoading:MutableState<Boolean>,
    onClick: (Int) -> Unit
) {
    Box(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
        AutoSlidingCarousel(itemsCount = viewModel.state.value.banners.size) {
            val banner = viewModel.state.value.banners[it]
            val image = getImage(url = banner.image, onSuccess = { /*TODO*/ }) {

            }
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clickable { onClick(banner.id) }) {
                Image(painter = image, contentDescription = "", modifier = Modifier.fillMaxSize(), contentScale = ContentScale.FillBounds)
            }
        }
        if (viewModel.state.value.bannersLoading)
            FeaturedCategoryLoader(shimmer = shimmer)
    }
//    if (viewModel.state.value.bannersLoading){
//        FeaturedCategoryLoader(shimmer = shimmer)
//    }else {
//
//    }
}