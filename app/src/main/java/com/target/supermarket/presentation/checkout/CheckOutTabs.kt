package com.target.supermarket.presentation.checkout

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.target.supermarket.ui.theme.lighterGray
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun CheckOutTabs(modifier: Modifier  = Modifier.fillMaxWidth(), pagerState: PagerState) {
    val tabs = listOf("Delivery Address", "Payment")
    Surface(modifier = modifier.padding(horizontal = 8.dp), shape = RoundedCornerShape(12.dp), color = lighterGray) {
        TabRow(selectedTabIndex = pagerState.currentPage, backgroundColor = Color.Transparent, modifier = Modifier.padding(8.dp)) {
            tabs.forEachIndexed() { idx, tab ->
                Tab(
                    selected = pagerState.currentPage == idx,
                    onClick = {},
                    text = {
                        Text(
                            text = tab,
                            style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        )
                    })
            }
        }
    }
}