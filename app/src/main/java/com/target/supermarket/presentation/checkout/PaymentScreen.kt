package com.target.supermarket.presentation.checkout

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.target.supermarket.R
import com.target.supermarket.presentation.commons.SearchTextInputBordered
import com.target.supermarket.presentation.viewModels.CheckOutContract
import com.target.supermarket.presentation.viewModels.CheckOutViewModel
import com.target.supermarket.ui.theme.baseColor
import com.target.supermarket.ui.theme.lighterGray
import com.target.supermarket.ui.theme.surfaceSilver
import com.target.supermarket.ui.theme.topAppBar

@Composable
fun PaymentScreen(modifier: Modifier = Modifier, checkOutViewModel: CheckOutViewModel, amount:String) {
    val icons:List<Int> = listOf(R.drawable.cod,R.drawable.credits,R.drawable.momo,R.drawable.airtel)
    LazyColumn(modifier = modifier
        .fillMaxSize()
        .background(color = topAppBar)){
        item {
            SectionHeader(header = "Payment Method")
            Column() {
                checkOutViewModel.state.value.methods.forEachIndexed() {idx, method->
                    PaymentMethods(method = method.first, isSelected = method.second, rId = icons[idx]){
                        checkOutViewModel.setEvent(CheckOutContract.CheckOutEvent.OnPaymentMethodChanged(method))
                    }
                }
            }
        }
        item { Spacer(modifier = Modifier.height(20.dp)) }
        item {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp)) {
                val number = "Airtel Money"
                Text(text = "Related Information", style = MaterialTheme.typography.h5, modifier = Modifier.padding(8.dp))
                Surface(shape = RoundedCornerShape(18.dp), color = lighterGray, modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp), elevation = 4.dp) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Enter Your $number", style = MaterialTheme.typography.caption.copy(fontSize = 18.sp, color = baseColor))
                        SearchTextInputBordered(modifier = Modifier.padding(vertical = 4.dp, horizontal = 12.dp), title = "$number number", error = "", value = ""){}
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Notes", style = MaterialTheme.typography.h6, modifier = Modifier.padding(vertical = 8.dp))
                Surface(shape = RoundedCornerShape(18.dp), color = lighterGray, modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp), elevation = 4.dp) {
                    Text(text = "An approval request message will be sent to the number above, when approved by pin a total of $amount will be deducted from that $number account",
                    style = MaterialTheme.typography.caption.copy(fontSize = 16.sp), modifier = Modifier.padding(16.dp))
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun PaymentMethods(modifier: Modifier = Modifier, method:String, isSelected:Boolean,rId:Int, onSelect:()->Unit) {
    Surface(modifier = modifier.padding(8.dp), shape = RoundedCornerShape(12.dp)) {
        Column(Modifier.fillMaxWidth()) {
            Row(modifier = Modifier
                .padding(end = 8.dp)
                .fillMaxWidth()
                .clickable { onSelect() }
                .background(color = Color.White), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(painter = painterResource(id = rId), contentDescription = "", modifier = Modifier.size(40.dp), contentScale = ContentScale.FillBounds)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = method, modifier = Modifier.padding(16.dp), fontWeight = FontWeight.ExtraBold)
                }
                Selector(isSelected = isSelected)
            }

        }
    }
}

@Composable
fun Selector(modifier: Modifier = Modifier, isSelected:Boolean) {
    Surface(modifier = modifier.size(20.dp), shape = CircleShape, color = surfaceSilver, elevation = 2.dp) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            if (isSelected)
                Icon(imageVector = Icons.Default.Check, contentDescription = "", modifier = Modifier.size(15.dp))
        }
    }
}