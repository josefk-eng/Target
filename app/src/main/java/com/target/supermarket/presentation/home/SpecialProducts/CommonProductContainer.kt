package com.target.supermarket.presentation.home.SpecialProducts

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.target.supermarket.R
import com.target.supermarket.domain.models.LocalProduct
import com.target.supermarket.presentation.commons.getImage
import com.target.supermarket.presentation.home.deals.AddButton
import com.target.supermarket.presentation.viewModels.CommonContract
import com.target.supermarket.presentation.viewModels.CommonViewModel
import com.target.supermarket.utilities.Loader
import com.target.supermarket.domain.models.Product
import com.target.supermarket.utilities.Constants
import com.valentinilk.shimmer.Shimmer

@Composable
fun CommonProductContainer(modifier: Modifier = Modifier, product: LocalProduct, color: Color, viewModel: CommonViewModel, shimmer: Shimmer, onClick:()->Unit) {
    var loader by rememberSaveable{ mutableStateOf(true) }
    val image = getImage(url = product.image, onSuccess = {loader=false}){
        loader = false
    }
    Surface(modifier = modifier
        .clickable { onClick() }, shape = RoundedCornerShape(16.dp), color = color) {
        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(modifier = Modifier.fillMaxWidth().height(200.dp)) {
                Image(painter = image, contentDescription = "", modifier = Modifier
                    .fillMaxSize(), contentScale = ContentScale.FillBounds)
                Loader(shimmer = shimmer, isLoading = loader)
            }
//            Stars()
            Text(
                text = product.name,
                style = MaterialTheme.typography.h6,
                modifier = Modifier
                    .widthIn(max = 150.dp)
                    .padding(vertical = 8.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(text = "In Stock")
            AdditionBar(modifier = Modifier.padding(vertical = 8.dp), viewModel = viewModel, product = product)
        }
    }
}

@Composable
fun AdditionBar(modifier: Modifier = Modifier, viewModel: CommonViewModel, product: LocalProduct) {
    Surface(shape = RoundedCornerShape(20.dp), color = Color.LightGray, modifier = modifier
        .wrapContentHeight()
        .width(160.dp)) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically) {
            AddButton(icon = painterResource(id = R.drawable.dash)){
                viewModel.setEvent(CommonContract.CommonEvent.RemoveFromCart(product))
            }
            Text(text = "${viewModel.state.value.cartItems.firstOrNull { it.id == product.id }?.qty ?: 0}", style = MaterialTheme.typography.h6)
            AddButton {
                viewModel.setEvent(CommonContract.CommonEvent.AddToCart(product))
            }
        }
    }
}

@Preview
@Composable
fun PreviewSpecial() {
//    CommonProductContainer()
}
