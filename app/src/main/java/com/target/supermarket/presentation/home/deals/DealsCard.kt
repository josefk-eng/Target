package com.target.supermarket.presentation.home.deals

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.target.supermarket.presentation.interfacess.InterfaceThree
import com.target.supermarket.presentation.interfacess.InterfaceThreeUnScroll
import com.target.supermarket.presentation.loaders.InterfaceThreeUnScrollLoader
import com.target.supermarket.presentation.loaders.SemiFeaturedCategoryLoader
import com.target.supermarket.presentation.navigation.Screen
import com.target.supermarket.presentation.viewModels.CommonContract
import com.target.supermarket.presentation.viewModels.CommonViewModel
import com.target.supermarket.presentation.viewModels.HomeViewModel
import com.target.supermarket.utilities.CommonMethods
import com.target.supermarket.utilities.Items
import com.target.supermarket.utilities.ItemsLoadingStatus
import com.target.targetapp.presentation.home.deals.DealsTimer
import com.target.targetapp.utils.AutoScrollingLazyRow
import com.target.targetapp.utils.autoScroll
import com.valentinilk.shimmer.Shimmer
import dev.chrisbanes.snapper.ExperimentalSnapperApi
import dev.chrisbanes.snapper.LazyListSnapperLayoutInfo
import dev.chrisbanes.snapper.rememberLazyListSnapperLayoutInfo
import kotlinx.coroutines.launch

@OptIn(ExperimentalSnapperApi::class)
@Composable
fun DealsCard(modifier: Modifier, viewModel: HomeViewModel, shimmer: Shimmer, navController: NavHostController) {
    val width = LocalConfiguration.current.screenWidthDp
    val scope = rememberCoroutineScope()
    Card(modifier = modifier
        .fillMaxWidth()
        .wrapContentHeight(),
        elevation = 0.dp,
        shape = RoundedCornerShape(bottomEnd = 16.dp, bottomStart = 16.dp, topEnd = 0.dp, topStart = 0.dp)
    ) {
        Column(modifier = Modifier.padding(horizontal = 8.dp)) {
            val rowState = rememberLazyListState()
            val layoutInfo: LazyListSnapperLayoutInfo = rememberLazyListSnapperLayoutInfo(rowState)
            LaunchedEffect(key1 = true){
                scope.launch {
                    while (true) {
                        rowState.autoScroll(SCROLL_DX = width.toFloat())
                    }
                }
            }
            DealsTimer(
                bigHeader = "Best Deals Of This Week",
                description = "A virtual Assistant collects the products from your list",
                modifier = Modifier.padding(vertical = 16.dp)
            )
            val products = viewModel.state.value.deals
            val special = viewModel.state.value.special
            if (viewModel.state.value.loaders[Items.Deals] == ItemsLoadingStatus.Loading){
                InterfaceThreeUnScrollLoader(shimmer = shimmer)
            }else {
                InterfaceThreeUnScroll(
                    modifier = Modifier,
                    navController = navController,
                    shimmer = shimmer,
                    products = products
                )
            }

            if (viewModel.state.value.loaders[Items.Special] == ItemsLoadingStatus.Loading){
                InterfaceThreeUnScrollLoader(shimmer = shimmer)
            }else if (special.isNotEmpty()) {
                AutoScrollingLazyRow(
                    list = special,
                    SCROLL_DX = width.toFloat(),
                    REQUIRED_CARD_COUNT = 8
                ) {
                    ProductContainer(modifier = Modifier
                        .width((width / 2).dp)
                        .padding(8.dp)
                        .clickable {
//                                viewModel.setEvent(CommonContract.CommonEvent.ChooseProduct(it))
                            CommonMethods.navigate(
                                navController = navController,
                                Screen.Product.passArg(it.id)
                            )
                        }, item = it, shimmer = shimmer
                    )
                }
            }
        }
    }
}