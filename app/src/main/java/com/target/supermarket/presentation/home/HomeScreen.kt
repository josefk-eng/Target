package com.target.supermarket.presentation.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.rememberImagePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.target.supermarket.R
import com.target.supermarket.domain.models.Category
import com.target.supermarket.presentation.commons.TopBarWidget
import com.target.supermarket.presentation.commons.getImage
import com.target.supermarket.presentation.home.SpecialProducts.CommonProductContainer
import com.target.supermarket.presentation.home.SpecialProducts.SpecialProductsView
import com.target.supermarket.presentation.home.containers.CatTabs
import com.target.supermarket.presentation.home.containers.FeaturedContainers
import com.target.supermarket.presentation.home.deals.DealsCard
import com.target.supermarket.presentation.home.drawer.DrawerHeader
import com.target.supermarket.presentation.interfacess.InterfaceOne
import com.target.supermarket.presentation.interfacess.NothingFound
import com.target.supermarket.presentation.loaders.Categories
import com.target.supermarket.presentation.loaders.InterfaceTwoLoader
import com.target.supermarket.presentation.loaders.SemiFeaturedCategoryLoader
import com.target.supermarket.presentation.loaders.SpecialProductsLoader
import com.target.supermarket.presentation.navigation.Screen
import com.target.supermarket.presentation.viewModels.CommonContract
import com.target.supermarket.presentation.viewModels.CommonViewModel
import com.target.supermarket.presentation.viewModels.HomeViewModel
import com.target.supermarket.ui.theme.baseColor
import com.target.supermarket.ui.theme.lightBlue
import com.target.supermarket.utilities.CommonMethods
import com.target.supermarket.utilities.Items
import com.target.supermarket.utilities.ItemsLoadingStatus
import com.target.targetapp.presentation.home.containers.SemiFeaturedCategory
import com.target.targetapp.presentation.home.drawer.DrawerMenu
import com.valentinilk.shimmer.Shimmer
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: CommonViewModel,
    topNavHost: NavHostController
) {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()
    val drawerState = scaffoldState.drawerState
    val shimmerInstance = rememberShimmer(shimmerBounds = ShimmerBounds.Window)
    val homeViewModel:HomeViewModel = hiltViewModel()
    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {
        TopBarWidget(viewModel = viewModel, navHostController = navController){ scope.launch { if (drawerState.isOpen) drawerState.close() else drawerState.open() } }
    },
        drawerContent = {
            DrawerHeader(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.3f))
            DrawerMenu(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(), navController = topNavHost, drawerState = drawerState, scope = scope)
        },
        drawerShape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp),
        scaffoldState = scaffoldState,
        backgroundColor = Color.Transparent
    ) {
        val width = LocalConfiguration.current.screenWidthDp
        Column(
            Modifier
                .fillMaxSize()
                .padding(it)) {
//                CatTabs(viewModel = viewModel, shimmer = shimmerInstance, navController = navController)
//                InterfaceThree(modifier = Modifier.fillMaxSize(), viewModel = viewModel)
                Box(Modifier.fillMaxSize()) {
                    if (viewModel.state.value.searching || viewModel.state.value.searchResult != null){
                        SearchArea(viewModel = viewModel, shimmer = shimmerInstance, navController = navController)
                    }else {
//                        val products = viewModel.state.value.productsReload?.collectAsLazyPagingItems()
                        LazyColumn(modifier = Modifier
                            .fillMaxSize()) {
                            item {
                                FeaturedContainers(viewModel = homeViewModel, shimmer = shimmerInstance, topNaHost = topNavHost)
                                DealsCard(modifier = Modifier, viewModel = homeViewModel, shimmer = shimmerInstance, topNavHost)
//                                if (viewModel.state.value.loaders[Items.Offer] == ItemsLoadingStatus.Loading){
//                                    SpecialProductsLoader(shimmer = shimmerInstance)
//                                }else {
//                                    SpecialProductsView(
//                                        modifier = Modifier,
//                                        containerColor = Color.White,
//                                        items = viewModel.state.value.special, navController = navController, viewModel = viewModel, shimmer = shimmerInstance, notOffer = false)
//                                }
//                                if (viewModel.state.value.loaders[Items.Recommended] == ItemsLoadingStatus.Loading){
//                                    SemiFeaturedCategoryLoader(shimmer = shimmerInstance)
//                                }else{
//                                    SemiFeaturedCategory(modifier = Modifier.fillMaxWidth(), shimmer = shimmerInstance, viewModel.state.value.banners.getOrNull(2)?.image ?: ""){
////                                    navController.navigate(Screen.Category.route)
//                                    }
//                                }
//                                if (viewModel.state.value.loaders[Items.Special] == ItemsLoadingStatus.Loading){
//                                    SpecialProductsLoader(shimmer = shimmerInstance, isGrayed = false)
//                                }else
//                                {
//                                    SpecialProductsView(
//                                    modifier = Modifier,
//                                    header1 = "Best Sellers",
//                                    header2 = "Best Offers This Month",
//                                    color = Color.White,
//                                    items = viewModel.state.value.offers,
//                                    navController = navController,
//                                    viewModel = viewModel,
//                                    shimmer = shimmerInstance,
//                                    notOffer = true
//                                    )
//                                }
//
//                                if (viewModel.state.value.loaders[Items.Recommended] == ItemsLoadingStatus.Loading){
//                                    SemiFeaturedCategoryLoader(shimmer = shimmerInstance)
//                                }else
//                                {
//                                SemiFeaturedCategory(modifier = Modifier.fillMaxWidth(), shimmer = shimmerInstance,viewModel.state.value.banners.getOrNull(3)?.image ?: ""){
////                                    navController.navigate(Screen.Category.route)
//                                }
//                                }
                            }



//                            products?.let {
//                                val rem = width / 2
//                                val calW = if (rem > 250) (width/3).toInt()  else (width/2).toInt()
//                                items(products.itemSnapshotList.items.chunked(if (rem>250) 3 else 2)){product->
//                                    Row() {
//                                        for (p in product){
//                                            CommonProductContainer(modifier = Modifier
//                                                .width(calW.dp)
//                                                .wrapContentHeight()
//                                                .padding(vertical = 10.dp, horizontal = 4.dp),color = Color.LightGray.copy(alpha = 0.3f), product = p, viewModel=viewModel, shimmer = shimmerInstance){
//                                                viewModel.setEvent(CommonContract.CommonEvent.ChooseProduct(p))
//                                                CommonMethods.navigate(navController = navController, route = Screen.Product.route)
//                                            }
//                                        }
//                                    }
//                                }
//                            }



//                            item {
//                                products.apply {
//                                    when{
//                                        loadState.refresh is LoadState.Loading -> {
//                                            LoadingItems()
//                                        }
//                                        loadState.append is LoadState.Loading -> {
//                                            LoadingItems()
//                                        }
//                                        loadState.append is LoadState.Error -> {
//                                            ErrorLoading()
//                                        }
//                                    }
//                                }
//                            }
                        }
                    }
                }
            }
    }
}



@Composable
fun ErrorLoading(modifier: Modifier = Modifier) {
    Box(modifier = modifier
        .fillMaxWidth()
        .height(150.dp), contentAlignment = Alignment.Center) {
        Text(text = "Error Loading products")
    }
}

@Composable
fun LoadingItems(modifier: Modifier = Modifier) {
    Box(modifier = modifier
        .fillMaxWidth()
        .height(30.dp), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun SearchArea(modifier: Modifier = Modifier, viewModel: CommonViewModel, shimmer: Shimmer, navController: NavHostController) {
    Box(modifier = modifier
        .fillMaxWidth()
        .heightIn(min = 200.dp)
        .wrapContentHeight()){
        if (viewModel.state.value.searching) {
            LoadingWidget(modifier = Modifier.align(Alignment.Center), text = "Searching...")
        }else if (viewModel.state.value.searchResult?.products?.isEmpty() == true){
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center){
                NothingFound(size = 20.dp)
            }
        }else{
            Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Search results for ${viewModel.searchText.collectAsState().value}", style = MaterialTheme.typography.caption.copy(fontWeight = FontWeight.ExtraBold), modifier = Modifier.padding(vertical = 8.dp))
                InterfaceOne(modifier = modifier.fillMaxSize(), shimmer=shimmer, navController = navController, products = viewModel.state.value.searchResult?.products ?: emptyList())
            }
        }
    }
}


@Composable
fun LoadingWidget(modifier: Modifier, text:String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
        CircularProgressIndicator(modifier = Modifier)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, style = MaterialTheme.typography.caption)
    }
}

