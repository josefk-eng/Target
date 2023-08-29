package com.target.supermarket.presentation.cart

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.target.supermarket.presentation.viewModels.CommonViewModel
import com.target.supermarket.ui.theme.lightBlue
import com.target.supermarket.ui.theme.lighterGray
import com.target.supermarket.utilities.CommonMethods.comma

@Composable
fun CartList(modifier: Modifier = Modifier.fillMaxSize(), viewModel: CommonViewModel, onCheck: () -> Unit) {
    Box(modifier = modifier, contentAlignment = Alignment.BottomCenter) {
        LazyColumn(modifier = modifier.fillMaxSize()){
            items(viewModel.state.value.cartItems){ product->
                CartItem(product = product, viewModel = viewModel)
            }
        }
        CartFooter(stage = "CheckOut", onCheck = onCheck, viewModel = viewModel)
    }
}


@Composable
fun CartFooter(modifier: Modifier = Modifier.fillMaxWidth(), stage:String, viewModel: CommonViewModel, onCheck:()->Unit) {
    var expand by rememberSaveable{ mutableStateOf(false) }
    Surface(modifier = modifier, color = Color.Transparent) {
        Column {
            AnimatedVisibility(visible = expand) {
                CartDetails(cart = viewModel.state.value.cartItems)
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = lighterGray),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    SmallButton(if (expand) Icons.Default.ArrowDropDown else Icons.Default.Info){ expand = !expand}
                    Text(
                        text = "Total:   ${viewModel.state.value.cartItems.sumOf { it.price*it.qty }.toInt().comma()}",
                        style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.ExtraBold)
                    )
                }
                TextButtonCustom(title = stage, onClick = onCheck, modifier = Modifier.padding(8.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SmallButton(imageVector: ImageVector, color: Color = Color.White, onClick:()->Unit) {
//    Surface() {
//        Icon(imageVector = imageVector, contentDescription = "", tint = lightBlue)
//    }
    IconButton(onClick,
        Modifier, true) {
        Icon(imageVector = imageVector, contentDescription = "")
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TextButtonCustom(title:String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        color = lightBlue,
        contentColor = MaterialTheme.colors.primary,
        border = ButtonDefaults.outlinedBorder
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = title,
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 14.dp),
                color = Color.White
            )
        }
    }
}

@Preview
@Composable
fun Preved() {
//    CartFooter(stage = "CheckOut"){}
}