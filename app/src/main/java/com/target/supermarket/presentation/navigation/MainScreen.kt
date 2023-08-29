package com.target.supermarket.presentation.navigation

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.target.supermarket.R
import com.target.supermarket.domain.usecases.main.GetCategoryById
import com.target.supermarket.presentation.accounts.Login
import com.target.supermarket.presentation.commons.BottomNavBar
import com.target.supermarket.presentation.commons.TopLevelBar
import com.target.supermarket.presentation.commons.getImage
import com.target.supermarket.presentation.dialogs.AccountsDialog
import com.target.supermarket.presentation.dialogs.NotificationDialog
import com.target.supermarket.presentation.notifications.Notification
import com.target.supermarket.presentation.viewModels.CommonContract
import com.target.supermarket.presentation.viewModels.CommonViewModel
import com.target.supermarket.ui.theme.baseColor
import com.target.supermarket.ui.theme.lightBlue
import com.target.supermarket.utilities.CartHolder
import com.target.supermarket.utilities.CommonMethods
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: CommonViewModel,
    topController: NavHostController,
    orderId: Int? = null
) {
    val navController = rememberNavController()
    val bottomBarHeight = 55.dp
    val bottomBarHeightPx = with(LocalDensity.current) {
        bottomBarHeight.roundToPx().toFloat()
    }
    val bottomBarOffsetHeightPx = remember { mutableStateOf(0f) }
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                val delta = available.y
                val newOffset = bottomBarOffsetHeightPx.value + delta
                bottomBarOffsetHeightPx.value =
                    newOffset.coerceIn(-bottomBarHeightPx, 0f)
                return Offset.Zero
            }
        }
    }
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = viewModel.state.value.to){
        if (viewModel.state.value.to){
            delay(1000)
            CommonMethods.navigate(navController,route = BottomScreenItem.Order.passArg(true,orderId))
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxWidth()
            .nestedScroll(nestedScrollConnection),
        scaffoldState = scaffoldState,
        bottomBar = { BottomAppBar(
            backgroundColor = Color.Transparent,
            elevation = 0.dp,
            modifier = Modifier
                .height(bottomBarHeight)
                .offset { IntOffset(x = 0, y = -bottomBarOffsetHeightPx.value.roundToInt()) }) {
            BottomNavBar(navController = navController)
        }},
        topBar = {
            TopLevelBar(viewModel = viewModel){
                viewModel.setEvent(CommonContract.CommonEvent.OpenNotificationDialog(true){})
            }
        },
        backgroundColor = Color.Transparent
    ) {
        val startDestination = BottomScreenItem.Home.route
        BottomScreenNav(topNavController = topController,navController = navController, viewModel = viewModel, modifier = Modifier
            .padding(it)
            .fillMaxSize()
            .navigationBarsPadding(), startDestination = startDestination)
        if (viewModel.state.value.accDialogStatus)
            AccountsDialog(onDismiss = {viewModel.setEvent(CommonContract.CommonEvent.OpenAccDialog(false))}) {
                Login(viewModel = viewModel)
            }
        if (viewModel.state.value.notificationDialogStatus)
            NotificationDialog(onDismiss = {viewModel.setEvent(CommonContract.CommonEvent.OpenNotificationDialog(false){})}) {
                Notification(viewModel = viewModel, controller = topController)
            }
    }
}
