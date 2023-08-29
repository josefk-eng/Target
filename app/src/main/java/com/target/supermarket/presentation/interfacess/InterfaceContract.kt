package com.target.supermarket.presentation.interfacess

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.target.supermarket.R
import com.target.supermarket.domain.models.Category
import com.target.supermarket.domain.models.LocalProduct
import com.target.supermarket.domain.models.Product
import com.target.supermarket.presentation.commons.LoadingOverlay
import com.target.supermarket.presentation.commons.ProgressIndicatorLoading
import com.target.supermarket.presentation.viewModels.CommonViewModel
import com.target.supermarket.ui.theme.lightBack
import com.target.supermarket.ui.theme.lightBlue
import com.target.supermarket.ui.theme.lighterGray
import com.valentinilk.shimmer.Shimmer

@Composable
fun InterfaceContract(modifier: Modifier, viewModel:CommonViewModel,navController:NavHostController, shimmer: Shimmer, category: Category, products:List<LocalProduct>) {
    if (products.isEmpty()){
        Box(modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
            NothingFound()
        }
    }else{
        when(category.ui){
//            "one"-> InterfaceOne(modifier = modifier.fillMaxSize(), shimmer=shimmer, navController = navController, products = products)
            "two"-> InterfaceTwo(modifier = modifier.fillMaxSize(), viewModel = viewModel, shimmer=shimmer, navController = navController, products = products)
            else-> InterfaceThree(modifier = modifier.fillMaxSize(), viewModel = viewModel, shimmer=shimmer, navController = navController, products = products)
        }
    }
}


@Composable
fun NothingFound(size:Dp = 150.dp) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(painter = painterResource(id = R.drawable.nothing_found), contentDescription = "", modifier = Modifier.size(size), tint = Color.LightGray)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Nothing Found", style = MaterialTheme.typography.h5)
    }
}