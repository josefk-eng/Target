package com.target.supermarket.presentation.product

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import com.target.supermarket.R
import com.target.supermarket.domain.models.LocalProduct
import com.target.supermarket.presentation.commons.getImage
import com.target.supermarket.domain.models.Product
import com.target.supermarket.utilities.FavIcon
import com.target.supermarket.utilities.FavIconInt

@OptIn(ExperimentalCoilApi::class)
@Composable
fun Product(modifier: Modifier = Modifier, nav:NavHostController, item: LocalProduct?) {
    val image = getImage(url = item?.image ?: "", onSuccess = {}){}
    val height = LocalConfiguration.current.screenHeightDp
    Surface(
        border = BorderStroke(1.dp, Color.LightGray),
        elevation = 4.dp,
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
    ) {
        Box(Modifier.fillMaxSize()) {
            Image(painter = image, contentDescription = "", modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .height((height / 2).dp)
                .padding(16.dp), contentScale = ContentScale.FillBounds)
            FavIcon(modifier = Modifier.align(Alignment.TopStart), Icons.Default.ArrowBack){
                nav.popBackStack()
            }
            Row(modifier = Modifier.align(Alignment.TopEnd)) {
                FavIcon(modifier = Modifier,Icons.Default.FavoriteBorder){

                }
                FavIconInt(modifier = Modifier,R.drawable.share_prod){

                }
            }
            Row(Modifier.fillMaxWidth().align(Alignment.BottomCenter), horizontalArrangement = Arrangement.SpaceBetween) {
                val modifierr = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                Card(shape = RoundedCornerShape(topEnd = 32.dp)) {
                    Text(text = item?.name ?: "", style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold), modifier = modifierr)
                }
                Card(shape = RoundedCornerShape(topStart = 32.dp)) {
                    Text(text = "${item?.price ?: "0.00"}/=", style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold), modifier = modifierr)
                }
            }
        }
    }
}