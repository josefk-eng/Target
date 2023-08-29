package com.target.supermarket.presentation.cart

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.target.supermarket.domain.models.CartItems
import com.target.supermarket.ui.theme.lightBack
import com.target.supermarket.ui.theme.lighterGray

@Composable
fun CartDetails(modifier: Modifier = Modifier, cart: List<CartItems>) {
    Surface(modifier = modifier
        .wrapContentHeight()
        .padding(10.dp)
        .fillMaxWidth(), color = lighterGray, shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)) {
        Column(
            Modifier
                .wrapContentHeight()
                .fillMaxWidth().padding(horizontal = 8.dp)) {
            Spacer(modifier = Modifier.height(16.dp))
            DetailsItem(title = "Sub-Total:", amount = "${cart.sumOf { it.price*it.qty }}",)
            DetailsItem(title = "Tax:", amount = "0.00/=")
            DetailsItem(title = "Delivery Fee:", amount = "0.00/=")
        }
    }
}

@Composable
fun DetailsItem(modifier: Modifier = Modifier, title: String, amount:String) {
    Row(modifier = modifier.padding(horizontal = 8.dp, vertical = 6.dp)) {
        Text(text = title, style = MaterialTheme.typography.caption.copy(fontSize = 15.sp, fontWeight = FontWeight.Bold), modifier = Modifier.weight(5f))
        Text(text = amount, style = MaterialTheme.typography.body1.copy(color = lightBack), modifier = Modifier.weight(5f))
    }
}


@Preview
@Composable
fun Prevd() {
//    CartDetails()
}