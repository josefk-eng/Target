package com.target.supermarket.presentation.product

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.target.supermarket.presentation.feature.ButtonWithRoundCorners
import com.target.supermarket.ui.theme.lighterGray
import com.target.supermarket.R
import com.target.supermarket.domain.models.LocalProduct
import com.target.supermarket.domain.models.Product
import com.target.supermarket.presentation.viewModels.CommonContract
import com.target.supermarket.presentation.viewModels.CommonViewModel

@Composable
fun ProductsButtons(modifier: Modifier = Modifier, viewModel: CommonViewModel, product: LocalProduct) {
    var isAdded by rememberSaveable{ mutableStateOf(false) }
    val times = rememberSaveable{ mutableStateOf(1) }
    Surface(color = lighterGray, shape = RoundedCornerShape(6.dp), modifier = Modifier.padding(8.dp)) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(8.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            if ((viewModel.state.value.cartItems.firstOrNull { it.id == product.id }?.qty ?: 0) > 0){
                Row(Modifier.heightIn(max = 50.dp).padding(vertical = 10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceEvenly) {
                    ButtonWithRoundCorners(icon = painterResource(id = R.drawable.dash), text = null){
                        if (times.value!=0){
                            times.value-=1
                        }else{
                            isAdded = false
                        }
                        viewModel.setEvent(CommonContract.CommonEvent.RemoveFromCart(product))
                    }
                    Text(text = "${viewModel.state.value.cartItems.firstOrNull { it.id == product.id }?.qty ?: 0}", modifier = Modifier.padding(horizontal = 16.dp), fontWeight = FontWeight.ExtraBold)
                    ButtonWithRoundCorners(icon = painterResource(id = R.drawable.plus),text = null) {
                        viewModel.setEvent(CommonContract.CommonEvent.AddToCart(product))
                    }
                }
            }else{
                Spacer(modifier = Modifier)
            }
            if ((viewModel.state.value.cartItems.firstOrNull { it.id == product.id }?.qty ?: 0) > 0){
                Spacer(modifier = Modifier)
            }else{
                ButtonWithRoundCorners(icon = null, text = "Add") {
                    viewModel.setEvent(CommonContract.CommonEvent.AddToCart(product))
                }
            }
        }
    }
}

@Preview
@Composable
fun Prev() {
//    ProductsButtons()
}