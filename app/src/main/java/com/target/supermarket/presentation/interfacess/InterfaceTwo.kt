package com.target.supermarket.presentation.interfacess

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.target.supermarket.R
import com.target.supermarket.domain.models.LocalProduct
import com.target.supermarket.domain.models.Product
import com.target.supermarket.presentation.commons.getImage
import com.target.supermarket.presentation.home.deals.AddButton
import com.target.supermarket.presentation.navigation.Screen
import com.target.supermarket.presentation.viewModels.CommonContract
import com.target.supermarket.presentation.viewModels.CommonViewModel
import com.target.supermarket.utilities.CommonMethods
import com.target.supermarket.utilities.CommonMethods.comma
import com.target.supermarket.utilities.Loader
import com.target.supermarket.utilities.MoneyFormatter
import com.valentinilk.shimmer.Shimmer

@Composable
fun InterfaceTwo(modifier: Modifier, viewModel: CommonViewModel, navController:NavHostController, shimmer:Shimmer, products:List<LocalProduct>) {
    val width = LocalConfiguration.current.screenWidthDp
    val rem = width / 2
    val calW = if (rem > 250) width/3  else (width/2.1).toInt()
    val height = calW * 1.2
    LazyColumn(modifier = modifier.fillMaxSize()){
        items(products.chunked(if (rem>250) 3 else 2) ){prod->
            Row() {
                for (p in prod){
                    val image = getImage(url = p.image, onSuccess = {}) {

                    }
                    Card(shape = RoundedCornerShape(topStart = 16.dp, bottomEnd = 16.dp, topEnd = 14.dp, bottomStart = 14.dp), modifier = Modifier
                        .padding(4.dp)
                        .width(calW.dp)
                        .height(height.dp).clickable {
                            viewModel.setEvent(CommonContract.CommonEvent.ChooseProduct(p))
                            CommonMethods.navigate(navController = navController, Screen.Product.route)
                        },) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Column(modifier = Modifier) {
                                Card(modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(10f)
                                    .padding(6.dp), shape = RoundedCornerShape(topStart = 16.dp, bottomEnd = 16.dp, topEnd = 14.dp, bottomStart = 14.dp)
                                ) {
                                    Box{
                                        Image(painter = image, contentDescription = "", modifier = Modifier
                                            .fillMaxSize(), contentScale = ContentScale.FillBounds)
                                        Loader(shimmer = shimmer, isLoading = viewModel.state.value.loading)
                                    }
                                }
                                Box(modifier = Modifier
                                    .weight(5f)
                                    ) {
                                    Text(text = p.name, style = MaterialTheme.typography.caption, maxLines = 2, modifier=Modifier.padding(8.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp)
                                        .align(Alignment.BottomCenter)) {
                                        MoneyFormatter(money = "${p.price.toInt().comma()}|${p.unit}")
                                        AddButton(icon = if(viewModel.state.value.cartItems.none { it.id == p.id }) painterResource(
                                            id = R.drawable.plus) else painterResource(id = R.drawable.dash)
                                        ) {
                                            if(viewModel.state.value.cartItems.none { it.id == p.id }){
                                                viewModel.setEvent(CommonContract.CommonEvent.AddToCart(p))
                                            }else{
                                                viewModel.setEvent(CommonContract.CommonEvent.RemoveFromCart(p))
                                            }
                                        }
                                    }
                                    Loader(shimmer=shimmer, isLoading = viewModel.state.value.loading)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}