package com.target.supermarket.presentation.commons

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.target.supermarket.R
import com.target.supermarket.presentation.checkout.CustomDialog
import com.target.supermarket.presentation.viewModels.CommonContract
import com.target.supermarket.presentation.viewModels.CommonViewModel
import com.target.supermarket.ui.theme.baseColor
import com.target.supermarket.ui.theme.lightBlue
import com.target.supermarket.utilities.Constants
import com.target.supermarket.utilities.Constants.addressError
import com.target.supermarket.utilities.DeliveryInfo

@Composable
fun TopBarWidget(modifier: Modifier = Modifier
    .fillMaxWidth()
    .wrapContentHeight(),
                 viewModel: CommonViewModel,
                 navHostController: NavHostController,
                 menuClick:()->Unit) {
    val error by Constants.connectionError.observeAsState("")
    Column(modifier = modifier.padding(horizontal = 6.dp)) {
        SearchableAppBar(navController = navHostController, leading = Icons.Default.Menu, leadingClick = menuClick, viewModel = viewModel)
        if (viewModel.state.value.remoteError.isNotEmpty() || error.isNotEmpty())
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(start = 16.dp)) {
                Icon(painter = painterResource(id = R.drawable.wifi), contentDescription = "", modifier = Modifier.size(20.dp))
                LazyRow(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp)
                ) {
                    item {
                        Text(
                            modifier = Modifier.padding(8.dp),
                            text = error.ifEmpty { viewModel.state.value.remoteError },
                            maxLines = 1,
                            style = MaterialTheme.typography.caption.copy(color = Color.Red)
                        )
                    }
                }
            }
    }


}


@Composable
fun TopLevelBar(modifier: Modifier = Modifier, viewModel: CommonViewModel, onNotClicked:()->Unit) {
    var onAddAddress by rememberSaveable{ mutableStateOf(false) }
    Column(modifier = Modifier.statusBarsPadding()) {
        val error by addressError.observeAsState("")
        Row(modifier = Modifier
            .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Row(modifier = Modifier.weight(10f), verticalAlignment = Alignment.CenterVertically) {
                val noAdd = viewModel.state.value.addresses.isEmpty()
                val address = viewModel.state.value.addresses.firstOrNull { it.autoCaptured }
                Text(text = "Deliveries to:", modifier = Modifier.padding(start = 10.dp, end = 10.dp), style = MaterialTheme.typography.caption.copy(fontWeight = FontWeight.ExtraBold, color = baseColor))
                if (noAdd || error.isNotEmpty()){
                    Text(text = error.ifEmpty { "No Address found" } , style = MaterialTheme.typography.caption.copy(
                        Color.White), maxLines = 2, modifier = Modifier
                        .padding(vertical = 8.dp), overflow = TextOverflow.Ellipsis)
                }
                else
                    if (address != null){
                    Text(text =
                    "${address.street} ${
                        address.street
                    }",
                        style = MaterialTheme.typography.caption.copy(color = lightBlue), maxLines = 2, modifier = Modifier
                            .padding(vertical = 8.dp), overflow = TextOverflow.Ellipsis )
                }else{
                    if (error.isNotEmpty())
                        Text(text = error , style = MaterialTheme.typography.caption.copy(
                            lightBlue), maxLines = 2, modifier = Modifier
                            .padding(vertical = 8.dp), overflow = TextOverflow.Ellipsis)
                }
                Spacer(modifier = Modifier.width(16.dp))
                IconButton(onClick = {
                                 onAddAddress = true
                    address?.let {
//                        viewModel.setEvent(CommonContract.CommonEvent.EditAddress(it))
                    }
                }) {
                    Icon(painter = painterResource(id = if (error.isNotEmpty()) R.drawable.add_location else R.drawable.edit_location), contentDescription = "", tint = Color.White, modifier = Modifier.size(25.dp))
                }
            }
            Box(modifier = Modifier
                .weight(2f)
                .padding(end = 8.dp)) {
                Box(modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.CenterEnd), contentAlignment = Alignment.Center) {
                    IconButton(onClick = { onNotClicked() }, modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp)) {
                        Icon(painter = painterResource(id = R.drawable.not), contentDescription = "")
                    }
                    Text(text = "${viewModel.state.value.notifications.size}", style = MaterialTheme.typography.caption.copy(fontWeight = FontWeight.ExtraBold))
                }
            }
        }
        Divider(modifier = Modifier
            .padding(bottom = 2.dp)
            .fillMaxWidth())
    }


    if (onAddAddress)
        CustomDialog(
            onNegativeClick = { onAddAddress = false }) {
//            NewAddress(viewModel = viewModel, modifier = Modifier.padding(16.dp))
            DeliveryInfo(modifier = Modifier, borrowNumber = {}){ onAddAddress = false }
        }
}