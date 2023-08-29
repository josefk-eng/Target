package com.target.supermarket.presentation.home.containers

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import com.target.supermarket.R
import com.target.supermarket.domain.models.Category
import com.target.supermarket.presentation.commons.getImage
import com.target.supermarket.presentation.loaders.Categories
import com.target.supermarket.presentation.navigation.Screen
import com.target.supermarket.presentation.viewModels.CommonContract
import com.target.supermarket.presentation.viewModels.CommonViewModel
import com.target.supermarket.ui.theme.lightBlue
import com.target.supermarket.utilities.CommonMethods
import com.target.supermarket.utilities.Items
import com.target.supermarket.utilities.ItemsLoadingStatus
import com.valentinilk.shimmer.Shimmer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CatTabs(modifier: Modifier = Modifier.fillMaxWidth(), viewModel: CommonViewModel, shimmer: Shimmer, navController: NavHostController) {
//        TabRow(selectedTabIndex = pagerState.currentPage, backgroundColor = topAppBar, divider = {}) {
    val imageLoading = remember{ mutableStateOf(true) }

    val context = LocalContext.current
    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier
        .fillMaxWidth()
        .padding(vertical = 16.dp)) {
        Box(modifier = Modifier
            .wrapContentWidth()
            .padding(8.dp)) {
            Text(text = context.getString(R.string.shop_by), style = MaterialTheme.typography.h6.copy(color = Color.DarkGray, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold))
        }
        val cats = (viewModel.state.value.searchResult?.depts ?: emptyList()).ifEmpty { viewModel.state.value.categories }
        if (viewModel.state.value.loaders[Items.Categories] is ItemsLoadingStatus.Error){
            imageLoading.value = false
            Text(text = (viewModel.state.value.loaders[Items.Categories] as ItemsLoadingStatus.Error).error, style = MaterialTheme.typography.caption)
        }else if (viewModel.state.value.loaders[Items.Categories] == ItemsLoadingStatus.Loading){
            Categories(shimmer = shimmer)
        }else{
            LazyRow(modifier = Modifier.padding(horizontal = 16.dp)) {

                item {
//                    cats.forEachIndexed() { idx, tab ->
//                        TabItem(color = lightBlue, cat = tab, modifier = Modifier
//                            .padding(horizontal = 4.dp, vertical = 8.dp)
//                            .clickable {
//                                viewModel.setEvent(CommonContract.CommonEvent.ChooseCategory(tab))
//                                CommonMethods.navigate(
//                                    navController = navController,
//                                    Screen.Category.passArg(catId = tab.id)
//                                )
//                            }, imageLoading = imageLoading
//                        )
//                    }
                }
            }

        }
    }
//        }
}

@Composable
fun TabItem(
    modifier: Modifier = Modifier,
    color: Color,
    cat: Category,
    imageLoading:MutableState<Boolean>
) {
    var onError by rememberSaveable{ mutableStateOf(false) }
    val image = getImage(url = cat.image, onSuccess = {
        onError = false
        imageLoading.value = false
    }) {
        onError = true
        imageLoading.value = false
    }
    Surface(shape = RoundedCornerShape(40.dp), color = color, modifier = modifier) {
        Box(modifier = Modifier.wrapContentSize()) {
            Row(horizontalArrangement = Arrangement.SpaceAround, verticalAlignment = Alignment.CenterVertically) {
                Card(shape = CircleShape, modifier = Modifier.size(40.dp)) {
                    if (onError){
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Icon(imageVector = Icons.Default.Info, contentDescription = "", tint = Color.Red, modifier = Modifier.size(20.dp))
                        }
                    }else {
                        Image(
                            painter = image,
                            contentDescription = "",
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(6.dp)
                        )
                    }
                }
                Text(
                    text = cat.name,
                    style = MaterialTheme.typography.caption.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    ),
                    modifier = Modifier.padding(vertical = 6.dp, horizontal = 10.dp)
                )
            }
        }
    }
}