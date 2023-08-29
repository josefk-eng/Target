package com.target.supermarket.presentation.cart

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.target.supermarket.domain.models.CartItems
import com.target.supermarket.presentation.commons.getImage
import com.target.supermarket.presentation.feature.ButtonWithRoundCorners
import com.target.supermarket.presentation.viewModels.CommonViewModel
import com.target.supermarket.ui.theme.lightBlue
import com.target.supermarket.ui.theme.lighterGray
import com.target.supermarket.R
import com.target.supermarket.presentation.home.deals.AddButton
import com.target.supermarket.presentation.viewModels.CommonContract
import com.target.supermarket.ui.theme.baseColor
import com.target.supermarket.utilities.CartQtyBtn
import com.target.supermarket.utilities.CommonMethods.comma

@Composable
fun CartItem(modifier: Modifier = Modifier, product:CartItems, viewModel: CommonViewModel) {
    val image = getImage(url = product.image, onSuccess = {}){}
    val context = LocalContext.current
    Row(modifier = modifier
        .fillMaxWidth()
        .padding(8.dp)
        .height(200.dp), verticalAlignment = Alignment.CenterVertically) {
        Surface(color = lighterGray, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth(0.7f)) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier
                        .fillMaxWidth(0.4f)
                        .fillMaxHeight()
                        .padding(4.dp)
                        .background(color = Color.White, shape = RoundedCornerShape(12.dp))) {
                    Image(painter = image, contentDescription = "",
                        Modifier
                            .fillMaxSize()
                            .padding(8.dp))
                }
                Box(modifier = Modifier.fillMaxSize()) {
                    Column(modifier = Modifier.align(Alignment.Center)) {
                        Text(text = product.name, style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.ExtraBold, fontSize = 16.sp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "@ ${product.price.toInt().comma()}/=", style = MaterialTheme.typography.body1.copy(color = baseColor))
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    Row(
                        Modifier
                            .heightIn(max = 50.dp)
                            .padding(vertical = 10.dp)
                            .align(Alignment.BottomCenter), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceEvenly) {

                        CartQtyBtn(modifier = Modifier, qty = product.qty, context = context, p = viewModel.state.value.products.first { it.id == product.id })
                    }
                }
            }
        }
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "Shs.${(product.price*product.qty).toInt().comma()}", style = MaterialTheme.typography.h6.copy(color = lightBlue))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Prev() {
//    CartItem()
}