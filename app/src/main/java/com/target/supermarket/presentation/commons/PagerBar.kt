package com.target.supermarket.presentation.commons

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.target.supermarket.utilities.CommonMethods
import com.target.supermarket.presentation.viewModels.CommonViewModel
import com.target.supermarket.ui.theme.topAppBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun PagerTopBar(modifier: Modifier = Modifier, pagerState: PagerState, navController: NavHostController, scope: CoroutineScope, viewModel: CommonViewModel) {
    TopAppBar(modifier = modifier.statusBarsPadding(),backgroundColor = topAppBar, elevation = 0.dp) {
        IconButton(onClick = { CommonMethods.pop(navController)}) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
        }
        HTabs(pagerState = pagerState, scope = scope, viewModel = viewModel)
    }
}


@OptIn(ExperimentalPagerApi::class)
@Composable
fun HTabs(modifier: Modifier = Modifier.fillMaxWidth(), pagerState: PagerState, scope: CoroutineScope, viewModel: CommonViewModel) {
    val tabs = listOf("My Basket (0)", "Save For Later")
    TabRow(selectedTabIndex = pagerState.currentPage, backgroundColor = topAppBar) {
        tabs.forEachIndexed() {idx, tab ->
            Tab(
                selected = pagerState.currentPage==idx,
                onClick = { scope.launch {
                    pagerState.animateScrollToPage(idx)
                } },
                text = { Text(text = tab, style = MaterialTheme.typography.caption.copy(fontWeight = FontWeight.Bold)) })
        }
    }
}