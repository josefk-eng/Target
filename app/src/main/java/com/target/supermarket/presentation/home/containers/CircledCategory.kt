package com.target.supermarket.presentation.home.containers

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.target.supermarket.R
import com.target.supermarket.domain.models.LocalProduct
import com.target.supermarket.domain.models.Product
import com.target.supermarket.presentation.commons.getImage
import com.target.supermarket.presentation.home.deals.AddButton
import com.target.supermarket.presentation.viewModels.CommonContract
import com.target.supermarket.presentation.viewModels.CommonViewModel
import com.target.supermarket.presentation.viewModels.HomeViewModel
import com.target.supermarket.ui.theme.lightBack
import com.target.supermarket.ui.theme.lighterGray
import com.target.supermarket.utilities.*
import com.target.supermarket.utilities.CommonMethods.comma
import com.valentinilk.shimmer.Shimmer

@Composable
fun CircledCategory(modifier: Modifier = Modifier, product: LocalProduct, shimmer: Shimmer, viewModel:HomeViewModel, isLoading:Boolean, onClick:()->Unit) {
    var loading by rememberSaveable{ mutableStateOf(true) }
    val context = LocalContext.current
    val cat = getImage(url = product.image, onSuccess = {
        loading = false
    }){
        loading = false
    }
    Column(modifier = modifier
        .wrapContentSize()
        .clickable { onClick() }, horizontalAlignment = Alignment.CenterHorizontally) {
        Box(modifier = Modifier
            .size(120.dp)
            .padding(8.dp)) {
            Surface(shape = CircleShape, modifier = Modifier
                .fillMaxSize(), border = BorderStroke(2.dp, lighterGray)
            ) {
                Cartable(product = product) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp)
                            .background(shape = CircleShape, color = Color.Transparent),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(painter = cat, contentDescription = "", contentScale = ContentScale.FillBounds)
//                        Loader(shimmer = shimmer, isLoading = isLoading)
                    }
                }
            }
//            CartAddButton(context = context, p = product)
        }
//        Spacer(modifier = Modifier.height(10.dp))
        Box(modifier = Modifier.widthIn(max = 100.dp)) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                text = product.name,
                style = MaterialTheme.typography.h6,
                    maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth(),
                    textAlign = TextAlign.Center
            )
                MoneyFormatter(money =  "${product.price.toDouble().toInt().comma()}/=")
            }
//            Loader(shimmer = shimmer, isLoading = loading)
        }
    }
}