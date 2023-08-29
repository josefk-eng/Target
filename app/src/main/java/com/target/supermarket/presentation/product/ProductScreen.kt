package com.target.supermarket.presentation.product

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavHostController
import com.target.supermarket.R
import com.target.supermarket.presentation.commons.SearchableAppBar
import com.target.supermarket.presentation.feature.AmountAndName
import com.target.supermarket.presentation.feature.ButtonAndName
import com.target.supermarket.presentation.viewModels.CommonViewModel
import com.target.supermarket.ui.theme.lightBlue
import com.target.supermarket.utilities.CartAddButton
import com.target.supermarket.utilities.CartQtyBtn
import com.target.supermarket.utilities.CommonMethods
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer

@Composable
fun ProductScreen(modifier: Modifier = Modifier, navController:NavHostController, id:Int) {
    val viewModel:ProductViewModel = hiltViewModel()
    val shimmer = rememberShimmer(shimmerBounds = ShimmerBounds.Window)
    val context = LocalContext.current
    LaunchedEffect(key1 = Unit){
        viewModel.setEvent(ProductsContract.ProductsEvent.GetProduct(id))
    }
    Scaffold(modifier = modifier) {
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(it)
            .statusBarsPadding()) {
            item {
                    Product(modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(), item = viewModel.state.value.product, nav = navController)
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(8.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    if (viewModel.state.value.cartItem != null && (viewModel.state.value.cartItem?.qty ?: 0) != 0 && viewModel.state.value.product != null) {
                        CartQtyBtn(
                            modifier = Modifier,
                            qty = viewModel.state.value.cartItem?.qty ?: 0,
                            context = context,
                            p = viewModel.state.value.product!!
                        )

                    }else { Spacer(modifier = Modifier.size(1.dp))}
                    viewModel.state.value.product?.let {p->
                        CartAddButton(context = context, p = p)
                    }
                }
//                viewModel.state.value.product?.let {
//                    ButtonAndName( modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(vertical = 8.dp, horizontal = 16.dp), item = it, cViewModel = cViewModel)
//                }
//                cViewModel.state.value.product?.let{
//                    ProductsButtons(viewModel = cViewModel, product = it)
//                }
                ReviewSection(modifier = Modifier)
//                RelatedProducts(modifier = Modifier.fillMaxWidth(), shimmer = shimmer, cViewModel.state.value.products.filter { it.id !=  cViewModel.state.value.product?.id}, viewModel = cViewModel)
            }
        }
    }
}

@Composable
fun ReviewSection(modifier: Modifier) {
    Column(modifier = Modifier.padding(18.dp)) {
        Text(text = "Product Review", style = MaterialTheme.typography.h5)
    }
}