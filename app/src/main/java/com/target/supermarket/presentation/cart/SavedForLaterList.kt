package com.target.supermarket.presentation.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.target.supermarket.presentation.viewModels.CommonViewModel
import com.target.supermarket.ui.theme.buttonInActive

@Composable
fun SavedForLater(modifier: Modifier = Modifier.fillMaxSize(), viewModel: CommonViewModel) {
    LazyColumn(modifier = modifier.fillMaxSize().background(color = buttonInActive)){

    }
}