package com.target.supermarket.presentation.product

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.target.supermarket.domain.models.LocalProduct
import com.target.supermarket.presentation.viewModels.CommonViewModel
import com.target.supermarket.domain.models.Product
import com.target.supermarket.presentation.home.deals.ProductContainer
import com.valentinilk.shimmer.Shimmer

@Composable
fun RelatedProducts(modifier: Modifier = Modifier, shimmer: Shimmer, items:List<LocalProduct>, viewModel: CommonViewModel) {
    val width = LocalConfiguration.current.screenWidthDp
    Column(modifier = modifier) {
        SectionHeader(header = "Related Products")
            LazyRow(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                items(items){item->
                    ProductContainer(modifier = Modifier.width((width/2).dp).padding(8.dp), item = item, shimmer = shimmer)
                }
        }
    }
}


@Composable
fun SectionHeader(modifier: Modifier = Modifier, header:String) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 12.dp, start = 16.dp)) {
        Text(text = header, style = MaterialTheme.typography.h6.copy(fontStyle = FontStyle.Italic))
    }
}