package com.target.supermarket.presentation.checkout

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.target.supermarket.domain.models.Product

@Composable
fun RelatedProducts(modifier: Modifier = Modifier, items:List<Product>) {
    val width = LocalConfiguration.current.screenWidthDp
    Column(modifier = modifier) {
        SectionHeader(header = "Related Products")
        for (i in 1..2){
            Row(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                items.forEach {item->
//                    ProductContainer(modifier = Modifier.width((width/2).dp).padding(8.dp), item = item)
                }
            }
        }
    }
}


@Composable
fun SectionHeader(modifier: Modifier = Modifier, header:String) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 12.dp, start = 16.dp)) {
        Text(text = header, style = MaterialTheme.typography.h6.copy(fontStyle = FontStyle.Italic, fontSize = 18.sp))
    }
}