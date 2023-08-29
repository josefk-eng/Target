package com.target.supermarket.presentation.checkout

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.target.supermarket.R
import com.target.supermarket.domain.models.PaymentNumbers
import com.target.supermarket.presentation.cart.CartFooter
import com.target.supermarket.presentation.commons.BottomScreens
import com.target.supermarket.presentation.commons.DialogBoxLoading
import com.target.supermarket.presentation.commons.SearchlessBar
import com.target.supermarket.presentation.navigation.BottomScreenItem
import com.target.supermarket.presentation.navigation.Screen
import com.target.supermarket.presentation.viewModels.CheckOutContract
import com.target.supermarket.presentation.viewModels.CheckOutViewModel
import com.target.supermarket.presentation.viewModels.CommonContract
import com.target.supermarket.presentation.viewModels.CommonViewModel
import com.target.supermarket.ui.theme.lightBlue
import com.target.supermarket.ui.theme.surfaceSilver
import com.target.supermarket.utilities.CommonMethods
import com.target.supermarket.utilities.CommonMethods.comma
import com.target.supermarket.utilities.InstallationUnique
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun CheckOut(navController: NavHostController, viewModel: CommonViewModel) {
    val pagerState = rememberPagerState(initialPage = 0)
    val state = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val btnText = rememberSaveable{ mutableStateOf("Confirm") }
    val checkOutViewModel:CheckOutViewModel = hiltViewModel()
    val context = LocalContext.current
    LaunchedEffect(key1 = Unit){
        checkOutViewModel.effect.collectLatest {ev->
            when(ev){
                is CheckOutContract.CheckOutEffect.OnPaymentComplete -> {
//                    CommonMethods.navigate(navController,Screen.Track.passArg(isNew = true, orderId = it.orderId))
//                    CommonMethods.navigate(navController,Screen.Track.route)
                    viewModel.setEvent(CommonContract.CommonEvent.FinalCleanUp)
                    val id = ev.orderId
                    CommonMethods.navigate(navController,BottomScreenItem.Home.passArg(to = true, orderId = id))
                }
                is CheckOutContract.CheckOutEffect.OnOrderException -> {
                    state.snackbarHostState.showSnackbar(message = ev.e.message ?: "An Exception has occurred")
                }
            }
        }
    }
    Scaffold(scaffoldState = state,
        topBar = {SearchlessBar(
            title = "CheckOut",
            leading = Icons.Default.ArrowBack,){
    if (pagerState.currentPage == 1){
        scope.launch {
            pagerState.animateScrollToPage(0)
        }
    }else{
        CommonMethods.pop(navController)
    }
    } }, backgroundColor = Color.Transparent) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(it)
            .navigationBarsPadding()) {
            CheckOutTabs(pagerState = pagerState)
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
                HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize(), count = 2) {position->
                    when(position){
                        0-> DeliveryScreen(viewModel = viewModel,checkOutViewModel = checkOutViewModel)
                        1-> Paye(viewModel = checkOutViewModel, state = state)//PaymentScreen(checkOutViewModel = checkOutViewModel, amount = viewModel.state.value.cartItems.sumOf { it.price*it.qty }.toInt().comma())
                    }
                }
                CartFooter(stage = btnText.value, viewModel = viewModel){
                    scope.launch {
                        if (pagerState.currentPage == 0){
                            if (checkOutViewModel.state.value.addresses.none {ad-> ad.isDefault }){
                                state.snackbarHostState.showSnackbar(message = "Please add or select default delivery information")
                            }else {
                                val number = checkOutViewModel.state.value.addresses.firstOrNull {ad-> ad.isDefault }?.phoneNumber!!
                                if (!checkOutViewModel.state.value.numbers.map { num-> num.number }.contains(number)){
                                    checkOutViewModel.setEvent(CheckOutContract.CheckOutEvent.OnAddNumber(
                                        PaymentNumbers(number = number, isActive = true)
                                    ))
                                }
                                btnText.value = "Order"
                                pagerState.animateScrollToPage(page = 1)
                            }
                        }
                        else {
                            val items = viewModel.state.value.cartItems.joinToString(separator = "#") {c->  "${c.id}" }
                            val address = checkOutViewModel.state.value.addresses.firstOrNull {a-> a.isDefault }
                            val token = InstallationUnique.id(context)
                            checkOutViewModel.setEvent(CheckOutContract.CheckOutEvent.OnPlaceOrder(
                                true,
                                price = viewModel.state.value.cartItems.sumOf { it.price*it.qty },
                                address = "${address?.village ?: ""}, ${address?.street ?: ""}, ${address?.parish ?: ""}, ${address?.division ?: ""}, ${address?.district ?: ""}",
                                contact = address?.phoneNumber ?: "",
                                identification = token ?: "",
                                items = items,
                                contactName = address?.contactName ?: "",
                            ))
                        }
                    }
                }
            }

            if (checkOutViewModel.state.value.paymentLoading)
                DialogBoxLoading()

            if (checkOutViewModel.state.value.showConfirmation)
                ConfirmationDialog(
                    number = checkOutViewModel.state.value.activeNumber?.number ?: "",
                    amount = viewModel.state.value.cartItems.sumOf {item-> item.price*item.qty }.toInt().comma(),
                onComfirm = { checkOutViewModel.setEvent(CheckOutContract.CheckOutEvent.OnConfirm) }) {
                    val items = viewModel.state.value.cartItems.joinToString(separator = "#") { "${it.id}" }
                    val address = viewModel.state.value.addresses.firstOrNull { it.isDefault }
                    val token = InstallationUnique.id(context)
                    checkOutViewModel.setEvent(CheckOutContract.CheckOutEvent.OnPlaceOrder(
                        false,
                        price = viewModel.state.value.cartItems.sumOf { it.price*it.qty },
                        address = "${address?.village ?: ""}, ${address?.street ?: ""}, ${address?.parish ?: ""}, ${address?.division ?: ""}, ${address?.district ?: ""}",
                        contact = address?.phoneNumber ?: "",
                        identification = token ?: "",
                        items = items,
                        contactName = address?.contactName ?: "",
                    )
                    )
                }
        }
    }
}

@Composable
fun ConfirmationDialog(number:String, amount:String,onComfirm:()->Unit,onDismiss:()->Unit) {
    Dialog(onDismissRequest = { onDismiss() }, properties = DialogProperties(dismissOnClickOutside = false)) {
        Box(modifier = Modifier
            .background(shape = RoundedCornerShape(16.dp), color = Color.White)
            ){
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(16.dp)) {
                Icon(
                    painter = painterResource(id = R.drawable.mobile),
                    contentDescription = "",
                    tint = lightBlue,
                    modifier = Modifier.size(50.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "An approval message of $amount will be sent to $number which upon approval, the said amount will be deducted from the account")
                Box(modifier = Modifier.fillMaxWidth()){
                    Row(modifier = Modifier
                        .wrapContentWidth()
                        .align(Alignment.CenterEnd)) {
                        TextButton(onClick = { onDismiss() }) {
                            Text(text = "Cancel")
                        }
                        Button(onClick = { onComfirm() }) {
                            Text(text = "Proceed")
                        }
                    }
                }
            }
        }
    }
}