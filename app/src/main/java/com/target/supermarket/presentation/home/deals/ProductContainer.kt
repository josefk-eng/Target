package com.target.supermarket.presentation.home.deals

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import com.target.supermarket.R
import com.target.supermarket.domain.models.LocalProduct
import com.target.supermarket.presentation.commons.getImage
import com.target.supermarket.ui.theme.lightBlue
import com.target.supermarket.utilities.Loader
import com.target.supermarket.domain.models.Product
import com.target.supermarket.utilities.CartAddButton
import com.valentinilk.shimmer.Shimmer

@OptIn(ExperimentalCoilApi::class)
@Composable
fun ProductContainer(modifier: Modifier = Modifier, shimmer: Shimmer, item: LocalProduct) {
    var loader by rememberSaveable{ mutableStateOf(true) }
    val context = LocalContext.current
    val image = getImage(url = item.image, onSuccess = {
        loader = false
    }){
        loader = false
    }
    Surface(shape = RoundedCornerShape(12.dp), modifier = modifier.height(250.dp), color = Color.LightGray.copy(alpha = 0.3f),) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(), verticalArrangement = Arrangement.Bottom) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .weight(10f), contentAlignment = Alignment.Center){
                Image(painter = image, contentDescription = "", contentScale = ContentScale.FillBounds, modifier = Modifier.fillMaxSize())
                Loader(shimmer = shimmer, isLoading = loader)
            }
            Box(modifier = Modifier
                .fillMaxWidth()
                .weight(5f),) {
                Column(
                    Modifier
                        .align(Alignment.CenterStart)
                        .padding(horizontal = 16.dp)) {
                    Text(text = item.name, style = MaterialTheme.typography.h6.copy(fontSize = 16.sp))
                    Measurement(item = item)
                    Stars()
                }
                Loader(shimmer = shimmer, isLoading = loader)
                CartAddButton(context = context, p = item, modifier = Modifier.align(Alignment.BottomEnd))
            }
        }
    }
}

@Composable
fun Measurement(modifier: Modifier = Modifier, item: LocalProduct) {
    val style = MaterialTheme.typography.h6.copy(color = lightBlue, fontSize = 16.sp)
    Row(modifier = modifier
        .wrapContentHeight()
        .fillMaxWidth(0.8f)) {
        Text(text = "Shs.${item.price}", style = style, maxLines = 1, overflow = TextOverflow.Ellipsis)
//        Text(text = "", style = style)
//        Text(text = "/${item.unit}", style = style)
    }
}

@Composable
fun Stars(modifier: Modifier = Modifier) {
    Row(modifier = Modifier.wrapContentSize()) {
        for (i in 1..5){
            Icon(imageVector = Icons.Default.Star, contentDescription = "", modifier = Modifier
                .padding(end = 3.dp)
                .size(14.dp))
        }
    }
}

@Composable
fun AddButton(modifier: Modifier = Modifier, icon:Painter = painterResource(id = R.drawable.plus), onAdd:()->Unit) {
    Surface(shape = CircleShape, modifier = modifier
        .size(30.dp)
        .clickable { onAdd() }, elevation = 6.dp) {
        IconButton(onClick = { onAdd() }, modifier = Modifier.fillMaxSize()) {
            Icon(painter = icon, contentDescription = "", tint = lightBlue, modifier = Modifier.size(20.dp))
        }
    }
}

@Preview
@Composable
fun ProductPreview() {
//    ProductContainer()
}
