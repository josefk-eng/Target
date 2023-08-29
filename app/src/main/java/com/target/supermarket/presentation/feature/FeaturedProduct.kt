package com.target.supermarket.presentation.feature

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.target.supermarket.domain.models.LocalProduct
import com.target.supermarket.presentation.commons.getImage
import com.target.supermarket.presentation.viewModels.CommonContract
import com.target.supermarket.presentation.viewModels.CommonViewModel
import com.target.supermarket.ui.theme.buttonActive
import com.target.supermarket.ui.theme.buttonInActive
import com.target.supermarket.ui.theme.topAppBar
import com.target.supermarket.domain.models.Product
import com.target.supermarket.presentation.product.ProductsButtons
import com.target.supermarket.utilities.FavIcon

@Composable
fun FeaturedProduct(modifier: Modifier = Modifier,product:LocalProduct, viewModel:CommonViewModel, onClick: () -> Unit) {
    val image = getImage(url = product.image, onSuccess = {}){}
    Surface(
        border = BorderStroke(1.dp, Color.LightGray),
        elevation = 4.dp,
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .width(200.dp)
            .height(250.dp)
            .padding(8.dp)
            .clickable { onClick() }
    ) {

        Column(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.65f)) {
                FavIcon(modifier = Modifier.align(Alignment.TopStart), Icons.Default.FavoriteBorder){}
                Image(painter = image, contentDescription = "", modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(16.dp))
            }
            Column(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(
                    color = topAppBar
                )) {
                Column(Modifier.padding(8.dp)) {
                    AmountAndName(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp, horizontal = 4.dp),
                        item = product,
                        maxLines = 2
                    )
                    ActionButtons(modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp), qty = viewModel.state.value.cartItems.firstOrNull { it.id == product.id }?.qty ?: 0,
                        onRemove = {viewModel.setEvent(CommonContract.CommonEvent.RemoveFromCart(product))},
                        onAdd = {viewModel.setEvent(CommonContract.CommonEvent.AddToCart(product))})
                }
            }
        }

    }
}

@Composable
fun AmountAndName(modifier: Modifier, maxLines:Int = 1, item: LocalProduct) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = item.name, style = MaterialTheme.typography.caption.copy(fontWeight = FontWeight.Bold, fontSize = 16.sp), modifier = Modifier.fillMaxWidth(0.5f), overflow = TextOverflow.Ellipsis, maxLines = maxLines)
        Text(text = "${item.price}/=", style = MaterialTheme.typography.caption.copy(fontWeight = FontWeight.ExtraBold, color = Color.Gray, fontSize = 16.sp), modifier = Modifier.fillMaxWidth(), overflow = TextOverflow.Ellipsis, maxLines = 1)
    }
}

@Composable
fun ButtonAndName(modifier: Modifier,item: LocalProduct, cViewModel:CommonViewModel) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = "${item.price}/=", style = MaterialTheme.typography.caption.copy(fontWeight = FontWeight.ExtraBold, color = Color.Gray, fontSize = 16.sp), modifier = Modifier.fillMaxWidth(), overflow = TextOverflow.Ellipsis, maxLines = 1)
        ProductsButtons(viewModel = cViewModel, product = item)
    }
}


@Composable
fun ButtonWithRoundCorners(modifier: Modifier = Modifier,icon:Painter?,text:String?, onClick:()->Unit) {
        icon?.let {
            Button(
                onClick = {onClick()},
                colors = ButtonDefaults.buttonColors(backgroundColor = buttonInActive, contentColor = Color.White),
                shape = RoundedCornerShape(8.dp),
                modifier = modifier.size(60.dp)
            ) {
                Icon(painter = icon, contentDescription = "", )
            }
        }
        text?.let {
            Button(
                onClick = onClick,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = buttonActive, contentColor = Color.White),
                contentPadding = PaddingValues(2.dp)
            ) {
                Text(text = text, style = MaterialTheme.typography.caption)
            }
        }

}



@Preview(showBackground = true)
@Composable
fun Prev() {
//    ButtonWithRoundCorners(modifier = Modifier
//        .wrapContentSize()
//        .padding(8.dp), icon = painterResource(id = R.drawable.plus), text = null) {
//
//    }
//    FeaturedProduct(){}
}