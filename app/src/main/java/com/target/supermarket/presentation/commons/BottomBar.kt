package com.target.supermarket.presentation.commons

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.target.supermarket.presentation.navigation.BottomScreenItem
import com.target.supermarket.presentation.navigation.Screen
import com.target.supermarket.ui.theme.lightBack
import com.target.supermarket.ui.theme.lightBlue
import com.target.supermarket.utilities.CommonMethods

@Composable
fun BottomNavBar(modifier: Modifier = Modifier, navController:NavHostController) {
    val screens = listOf(
        BottomScreenItem.Home,
        BottomScreenItem.Order
    )
    BottomNavigation( elevation = 0.dp, modifier = modifier
        .fillMaxWidth()
        .padding(8.dp), backgroundColor = Color.Transparent, contentColor = lightBlue) {
        val currentRoute = navController.currentBackStackEntryAsState()
        screens.forEach{b->
            val route = if (b is BottomScreenItem.Home) b.passArg(false,-1) else  (b as BottomScreenItem.Order).passArg(false,-1)
            BottomNavigationItem(
                selected = currentRoute.value?.destination?.route == b.route,
                onClick = { CommonMethods.navigate(navController,route) },
                icon = { Icon(painter = painterResource(id = b.icon), contentDescription = "")},
                selectedContentColor = lightBlue, unselectedContentColor = lightBack,
//                label = { Text(text = b.title)}
            )
        }
    }
}

data class BottomScreens(
    val title:String,
    val icon:ImageVector,
    val route:String
)