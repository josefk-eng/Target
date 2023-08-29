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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.target.supermarket.domain.models.LocalProduct
import com.target.supermarket.domain.models.Product
import com.target.supermarket.presentation.commons.getImage
import com.target.supermarket.presentation.navigation.Screen
import com.target.supermarket.presentation.viewModels.CommonContract
import com.target.supermarket.presentation.viewModels.CommonViewModel
import com.target.supermarket.utilities.CommonMethods
import com.target.supermarket.utilities.Loader
import com.valentinilk.shimmer.Shimmer

@Composable
fun InterfaceThree(modifier: Modifier, viewModel: CommonViewModel, navController:NavHostController, shimmer: Shimmer, products:List<LocalProduct>) {
    val width = LocalConfiguration.current.screenWidthDp
    val rem = width / 2
    val calW = if (rem > 250) (width/3.1).toInt()  else (width/2.1).toInt()
    val height = calW * 1.2
    LazyColumn(modifier = modifier
        .fillMaxSize()
        .padding(8.dp)){
        items(products.chunked(if (rem>250) 3 else 2) ){prod->
            Row() {
                for (p in prod){
                    val image = getImage(url = p.image, onSuccess = {}) {

                    }
                    val order = if(p != prod.last() || (viewModel.state.value.inCategory.size%2 != 0 && p==viewModel.state.value.inCategory.last())) BorderOrder.End else BorderOrder.Start
                    Box(modifier = Modifier
                        .width(calW.dp)
                        .height(height.dp).clickable {
                            viewModel.setEvent(CommonContract.CommonEvent.ChooseProduct(p))
                            CommonMethods.navigate(navController = navController, Screen.Product.route)
                        }
                        .drawSegmentedBorder(
                            strokeWidth = 1.dp,
                            color = Color.LightGray,
                            borderOrder = order,
                            cornerPercent = 40,
                            drawDivider = !prod.contains(viewModel.state.value.inCategory.last())
                        ), contentAlignment = Alignment.Center) {
                        Column(modifier = Modifier) {
                            Card(modifier = Modifier
                                .fillMaxWidth()
                                .weight(10f)
                                .padding(10.dp), shape = RoundedCornerShape(topStart = 16.dp, bottomEnd = 16.dp, topEnd = 14.dp, bottomStart = 14.dp)
                            ) {
                                Box{
                                    Image(painter = image, contentDescription = "", modifier = Modifier
                                        .fillMaxSize(), contentScale = ContentScale.FillBounds)
                                    Loader(shimmer = shimmer, isLoading = viewModel.state.value.loading)
                                }
                            }
                            Box(modifier = Modifier
                                .weight(3f)
                                .padding(horizontal = 16.dp), contentAlignment = Alignment.Center) {
                                Text(text = p.name, style = MaterialTheme.typography.caption, maxLines = 2, textAlign = TextAlign.Center, overflow = TextOverflow.Ellipsis)
                            }
                        }
                    }
                }
            }
        }
    }
}