package com.target.supermarket

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.target.supermarket.domain.models.LocalProduct
import com.target.supermarket.domain.models.TargetNotification
import com.target.supermarket.domain.notifications.sendNotification
import com.target.supermarket.presentation.navigation.Screen
import com.target.supermarket.presentation.navigation.TopLevelNavigation
import com.target.supermarket.presentation.viewModels.CommonContract
import com.target.supermarket.presentation.viewModels.CommonViewModel
import com.target.supermarket.ui.theme.TargetTheme
import com.target.supermarket.ui.theme.lightBlue
import com.target.supermarket.ui.theme.lighterGray
import com.target.supermarket.utilities.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

//import dev.chrisbanes.accompanist.insets.ProvideWindowInsets

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    lateinit var client:FusedLocationProviderClient
    lateinit var addReceiver: BroadcastReceiver
    lateinit var removeReceiver: BroadcastReceiver
    lateinit var orderProgress: BroadcastReceiver
    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window,false)
        client = LocationServices.getFusedLocationProviderClient(this)
        setContent {
            val viewModel: CommonViewModel = hiltViewModel()
            val controller = rememberNavController();
            val context = LocalContext.current
            val broadCast = LocalBroadcastManager.getInstance(this)
            val stackEntry by controller.currentBackStackEntryAsState()

            addReceiver = object: BroadcastReceiver(){
                override fun onReceive(p0: Context?, p1: Intent?) {
                    p1?.let { intent ->
                        val p = intent.getParcelableExtra<LocalProduct>("product")
                        p?.let { localProduct->
                            viewModel.setEvent(CommonContract.CommonEvent.AddToCart(localProduct))
                        }
                    }
                }
            }
            removeReceiver = object: BroadcastReceiver(){
                override fun onReceive(p0: Context?, p1: Intent?) {
                    p1?.let { intent ->
                        val p = intent.getParcelableExtra<LocalProduct>("product")
                        p?.let { localProduct->
                            viewModel.setEvent(CommonContract.CommonEvent.RemoveFromCart(localProduct))
                        }
                    }
                }
            }

            LaunchedEffect(key1 = Unit){
                viewModel.effect.collectLatest {
                    when(it){
                        CommonContract.CommonEffect.OnCheckOut -> {}
                        is CommonContract.CommonEffect.OnLoadingFinished -> {}
                        is CommonContract.CommonEffect.OnSendNotification -> {
                            sendNotification(it.notification)
                        }
                    }
                }
            }

            broadCast.registerReceiver(addReceiver, IntentFilter("ADD"))
            broadCast.registerReceiver(removeReceiver, IntentFilter("REMOVE"))
//            ProvideWindowInsets {
            TargetTheme {
                    val geoPermLauncher =
                        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestMultiplePermissions()) { isGranted ->
                            if (isGranted.getValue(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                                client.lastLocation.addOnSuccessListener {
                                    if (it != null) {
                                        address(it.latitude, it.longitude, context) {
                                            viewModel.setEvent(
                                                CommonContract.CommonEvent.OnLocationFound(
                                                    it
                                                )
                                            )
                                        }
                                    }
                                }

                            } else {
                                Log.e("User Address", "Denied")
                            }
                        }
                    val error by Constants.connectionError.observeAsState("")
                    val systemUiController = rememberSystemUiController()
                    systemUiController.setStatusBarColor(color = Color.Transparent)
                    systemUiController.setNavigationBarColor(color = Color.Transparent)
                    LaunchedEffect(key1 = Unit) {
                        geoPermLauncher.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        )
                    }
                    Constants.connectionError.observe(this) {
                        viewModel.setEvent(CommonContract.CommonEvent.OnConnectionChanged(it.isNotEmpty() && viewModel.state.value.showErrorDialog))
                    }


                val sheetState = rememberModalBottomSheetState(
                    initialValue = ModalBottomSheetValue.Hidden,
                    confirmStateChange = { it != ModalBottomSheetValue.HalfExpanded }
                )
                val coroutineScope = rememberCoroutineScope()

                BackHandler(sheetState.isVisible) {
                    coroutineScope.launch { sheetState.hide() }
                }
                CartPReview(
                    modifier = Modifier
                        .fillMaxSize()
                        .navigationBarsPadding(),
                    sheetState = sheetState,
                    scope = coroutineScope,
                    onCheckOut = {
                        coroutineScope.launch {
                            delay(1000)
                            CommonMethods.navigate(controller, Screen.Cart.route)
                        }
                    },
                    viewModel = viewModel
                ) {
                    Scaffold(
                        Modifier
                            .fillMaxSize(),
                        floatingActionButton = {
                            if (viewModel.state.value.cartItems.isNotEmpty()
                                && !(stackEntry?.destination?.route ?: "").contains(Screen.Cart.route)
                                && !(stackEntry?.destination?.route ?: "").contains(Screen.CheckOut.route)
                                && !(stackEntry?.destination?.route ?: "").contains(Screen.Launcher.route))
                                Box(Modifier.padding(bottom = 40.dp)) {
                                    CartHolder(viewModel = viewModel, sheetState = sheetState, scope = coroutineScope)
                                }
                        }) {
                        TopLevelNavigation(
                            navController = controller,
                            viewModel = viewModel,
                            modifier = Modifier.padding(it)
                        )
                    }
                    if (viewModel.state.value.isConnected)
                        ErrorDialog(error = error) {
                            viewModel.setEvent(CommonContract.CommonEvent.DialogShown)
                        }
                }
                }
//            }
        }
    }

    override fun onDestroy() {
        unregisterReceiver(addReceiver)
        unregisterReceiver(removeReceiver)
        super.onDestroy()
    }
}


@Composable
fun ErrorDialog(error: String, onDismiss:()->Unit) {
    Dialog(onDismissRequest = { onDismiss() }, properties = DialogProperties(dismissOnClickOutside = false)) {
        Box(modifier = Modifier
            .background(shape = RoundedCornerShape(16.dp), color = Color.White)
            .size(300.dp)){
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.align(
                Alignment.Center)) {
                Icon(painter = painterResource(id = R.drawable.wifi), contentDescription = "", Modifier.size(100.dp))
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = error, style = MaterialTheme.typography.caption)
            }
            TextButton(onClick = { onDismiss() }, modifier = Modifier.align(Alignment.BottomEnd)) {
                Text(text = "Close")
            }
        }
    }
}




@Preview(showBackground = true)
@Composable
fun DefaultPreview() {

}