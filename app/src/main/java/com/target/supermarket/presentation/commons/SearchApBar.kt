package com.target.supermarket.presentation.commons

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.target.supermarket.R
import com.target.supermarket.presentation.navigation.Screen
import com.target.supermarket.presentation.viewModels.CommonContract
import com.target.supermarket.presentation.viewModels.CommonViewModel
import com.target.supermarket.ui.theme.baseColor
import com.target.supermarket.ui.theme.lighterGray

@Composable
fun SearchableAppBar(
    isSearch: Boolean = true,
    leading: ImageVector,
    modifier: Modifier = Modifier,
    title: String = "Here",
    navController: NavHostController,
    viewModel: CommonViewModel,
    ctrSearch:Boolean = false,
    leadingClick: () -> Unit
) {
    val searchVisible = rememberSaveable{ mutableStateOf(!ctrSearch) }
    val searchIcon = if (searchVisible.value) R.drawable.search_x else R.drawable.search
    TopAppBar(modifier = modifier,backgroundColor = Color.Transparent, elevation = 0.dp) {
        IconButton(onClick = leadingClick) {
            Icon(imageVector = leading, contentDescription = "", tint = baseColor)
        }
        if (isSearch){
            AnimatedVisibility(visible = searchVisible.value,modifier = Modifier.weight(10f)) {
                val text by viewModel.searchText.collectAsState()
                SearchTextInput(modifier = Modifier.fillMaxWidth(), title = title, value = text){
                    viewModel.searchText.value = it
                    viewModel.setEvent(CommonContract.CommonEvent.OnSearchTextChanged)
                }
            }
            AnimatedVisibility(visible = !searchVisible.value,modifier = Modifier.weight(10f)) {
                Spacer(modifier = Modifier.fillMaxWidth())
            }
        }else{
            Text(text = title, style = MaterialTheme.typography.h6,modifier = Modifier.weight(10f))
        }
        if (ctrSearch) {
            Box(modifier = Modifier.size(40.dp), contentAlignment = Alignment.TopCenter) {
                IconButton(
                    onClick = { searchVisible.value = !searchVisible.value }, modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp)
                ) {
                    Icon(painter = painterResource(id = searchIcon), contentDescription = "", tint = baseColor)
                }
            }
        }
        Box(modifier = Modifier.size(40.dp), contentAlignment = Alignment.TopCenter) {
            IconButton(onClick = {
               viewModel.setEvent(CommonContract.CommonEvent.OpenAccDialog(true))
            }, modifier = Modifier
                .fillMaxSize()
                .padding(4.dp)) {
                Icon(imageVector = Icons.Default.Person, contentDescription = "", tint = baseColor)
            }
        }
    }
}