package com.target.supermarket.presentation.checkout

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.target.supermarket.presentation.payments.MobileMoneyDetails
import com.target.supermarket.presentation.payments.PaymentTab1
import com.target.supermarket.presentation.viewModels.CheckOutViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Paye(modifier: Modifier = Modifier, viewModel: CheckOutViewModel, state: ScaffoldState) {
    val pagerState = rememberPagerState(initialPage = 0)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    Column(modifier = modifier
        .fillMaxSize()
        .padding(16.dp)) {
        PaymentTabs(scope = scope, pagerState = pagerState){idx->
            if (idx==0) {
                scope.launch {
                    pagerState.animateScrollToPage(idx)
                }
            }else{
                Toast.makeText(context,"Coming Soon....", Toast.LENGTH_LONG).show()
            }
        }
        Box(modifier = Modifier.fillMaxSize()) {
            HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize(), count = 1) {position->
                when(position){
                    0 -> MobileMoneyDetails(viewModel = viewModel, state = state, scope = scope)
                }
            }

//            Card(modifier = Modifier
//                .align(Alignment.BottomCenter)
//                .padding(8.dp), shape = RoundedCornerShape(16.dp), backgroundColor = lightBlue) {
//                Row(modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(8.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
//                    TextButton(onClick = { /*TODO*/ }) {
//                        Text(text = "Pay Now", style = MaterialTheme.typography.h6.copy(color = Color.White))
//                    }
//                    Text(text = "Shs 000.000/=", style = MaterialTheme.typography.caption.copy(fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.ExtraBold))
//                }
//            }
        }
    }
}


@OptIn(ExperimentalPagerApi::class)
@Composable
fun PaymentTabs(modifier: Modifier = Modifier, scope:CoroutineScope, pagerState:PagerState, onClick:(id:Int)->Unit) {
    val methods = listOf("momo","Credit/Debit","Cash On Delivery")
    val icons = listOf(com.target.supermarket.R.drawable.mobilemoney, com.target.supermarket.R.drawable.credit, com.target.supermarket.R.drawable.cashod)
    Surface(modifier = modifier.padding(horizontal = 8.dp), shape = RoundedCornerShape(12.dp), color = Color.Transparent, elevation = 0.dp) {
        LazyRow(modifier = Modifier.fillMaxWidth()){
            itemsIndexed(methods){idx,tab ->
                PaymentTab1(
                    modifier = Modifier.padding(8.dp).clickable { onClick(idx) },
                    isActive = pagerState.currentPage == idx,
                    tabText = tab,
                    resId = icons[idx]
                )
            }
        }
    }
}