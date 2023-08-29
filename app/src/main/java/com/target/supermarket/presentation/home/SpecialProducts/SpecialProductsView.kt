package com.target.supermarket.presentation.home.SpecialProducts

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.target.supermarket.domain.models.LocalProduct
import com.target.supermarket.presentation.navigation.Screen
import com.target.supermarket.presentation.viewModels.CommonContract
import com.target.supermarket.presentation.viewModels.CommonViewModel
import com.target.supermarket.utilities.CommonMethods
import com.target.supermarket.utilities.VerticalGrid
import com.target.supermarket.domain.models.Product
import com.target.targetapp.presentation.home.containers.SemiFeaturedCategory
import com.valentinilk.shimmer.Shimmer


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SpecialProductsView(
    modifier: Modifier,
    color: Color = Color.LightGray.copy(alpha = 0.2f),
    header1:String = "Awesome Shopping",
    header2: String = "Our Special Products",
    containerColor: Color =  Color.LightGray.copy(alpha = 0.3f),
    items:List<LocalProduct>,
    viewModel: CommonViewModel,
    shimmer: Shimmer,
    notOffer:Boolean,
    navController:NavHostController
) {
    val state = LazyListState()
    val width = LocalConfiguration.current.screenWidthDp

    Surface(color = color, modifier = modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(vertical = 8.dp)) {
        Column(modifier = Modifier.padding(4.dp)) {
            if (notOffer) {
            Text(text = header1,style = MaterialTheme.typography.h6.copy(color = Color.Magenta))
            Spacer(modifier = Modifier.height(12.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(text = header2, style = MaterialTheme.typography.h6, modifier = Modifier.fillMaxWidth(0.7f), maxLines = 1, overflow = TextOverflow.Ellipsis)
                TextTag(text = "View All")
            }
            }else {
                SemiFeaturedCategory(modifier = Modifier.fillMaxWidth(), shimmer = shimmer, url = com.target.supermarket.R.drawable.offer, height = 100.dp){
//                    navController.navigate(Screen.Category.route)
                }
            }
            VerticalGrid(
                modifier = Modifier
                    .fillMaxWidth(),
            ){
                items.forEach {product->
                    CommonProductContainer(modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp),color = containerColor, product = product, viewModel=viewModel, shimmer = shimmer){
                        viewModel.setEvent(CommonContract.CommonEvent.ChooseProduct(product))
                        CommonMethods.navigate(navController = navController, route = Screen.Product.route)
                    }
                }
            }

        }
    }
}

@Composable
fun TextTag(modifier: Modifier = Modifier, text:String ) {
    Box(contentAlignment = Alignment.Center, modifier = modifier
        .wrapContentSize()
        .border(2.dp, color = Color.Magenta, shape = RoundedCornerShape(16.dp))
        .clickable { }
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.caption.copy(
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
        )
    }
}