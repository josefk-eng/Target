package com.target.supermarket.presentation.category

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.target.supermarket.R
import com.target.supermarket.presentation.commons.CollapsibleScreen
import com.target.supermarket.presentation.commons.SearchableAppBar
import com.target.supermarket.presentation.commons.getImage
import com.target.supermarket.presentation.feature.FeaturedProduct
import com.target.supermarket.presentation.interfacess.InterfaceOne
import com.target.supermarket.presentation.navigation.Screen
import com.target.supermarket.presentation.viewModels.*
import com.target.supermarket.ui.theme.lightBlue
import com.target.supermarket.utilities.CollapsibleScreenNew
import com.target.supermarket.utilities.CommonMethods
import com.target.supermarket.utilities.Loader
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NewCategory(navController: NavHostController, mainViewModel:CommonViewModel, id:Int) {
    val viewModel:CategoryViewModel = hiltViewModel()
    val shimmer = rememberShimmer(shimmerBounds = ShimmerBounds.Window)
    val context = LocalContext.current
    LaunchedEffect(key1 = Unit){
        viewModel.setEvent(CategoryContract.CategoryEvent.LoadTagInfo(id))
        viewModel.effect.collectLatest {
            when(it){
                is CategoryContract.CategoryEffect.RemoteError -> {
                    Toast.makeText(context, it.e.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    val height = LocalConfiguration.current.screenHeightDp
    val image = getImage(url = viewModel.state.value.banner?.image ?: "", onSuccess = { /*TODO*/ }) {

    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(it)) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height((height * 0.35).dp)) {
                Image(painter = image, contentDescription = "", contentScale = ContentScale.FillBounds, modifier = Modifier.fillMaxSize())
                Loader(shimmer = shimmer, isLoading = viewModel.state.value.bannerLoader)
                  SearchableAppBar(modifier = Modifier.statusBarsPadding(),leading = Icons.Default.ArrowBack, navController = navController, viewModel = mainViewModel) {
                    navController.popBackStack()
                }
            }
            InterfaceOne(modifier = Modifier
                .fillMaxSize()
                .padding(top = (height * 0.25).dp), navController = navController, shimmer = shimmer, products = viewModel.state.value.products, loader = viewModel.state.value.productsLoading)
        }
    }


//    CollapsibleScreenNew(
//        navController = navController,
//        shimmer = shimmer,
//        imageUrl = viewModel.state.value.banner?.image ?: "",
//        imageLoader = viewModel.state.value.bannerLoader,
//        title = viewModel.state.value.tags.firstOrNull()?.name ?: ""
//    ) {
//        InterfaceOne(modifier = Modifier.fillMaxSize(), navController = navController, shimmer = shimmer, products = viewModel.state.value.products)
////        LazyVerticalGrid(
////            columns = GridCells.Adaptive(200.dp),
////            modifier = Modifier.fillMaxSize(),
////        ){
////            items(viewModel.state.value.products){item ->
////                FeaturedProduct(product = item, viewModel = viewModel){
////                    viewModel.setEvent(CommonContract.CommonEvent.ChooseProduct(item))
////                    CommonMethods.navigate(navController = navController, route = Screen.Product.route)
////                }
////            }
////        }
//    }
}