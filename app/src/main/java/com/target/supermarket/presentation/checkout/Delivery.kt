package com.target.supermarket.presentation.checkout

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import com.target.supermarket.R
import com.target.supermarket.domain.models.Address
import com.target.supermarket.domain.models.CartItems
import com.target.supermarket.domain.models.OrderDetails
import com.target.supermarket.presentation.commons.getImage
import com.target.supermarket.presentation.home.deals.AddButton
import com.target.supermarket.presentation.viewModels.CheckOutContract
import com.target.supermarket.presentation.viewModels.CheckOutViewModel
import com.target.supermarket.presentation.viewModels.CommonContract
import com.target.supermarket.presentation.viewModels.CommonViewModel
import com.target.supermarket.ui.theme.lightBack
import com.target.supermarket.ui.theme.lightBlue
import com.target.supermarket.utilities.CommonMethods.comma
import com.target.supermarket.utilities.DeliveryInfo
import com.target.supermarket.utilities.InstallationUnique
import kotlinx.coroutines.flow.collectLatest

@Composable
fun DeliveryScreen(
    modifier: Modifier = Modifier,
    viewModel: CommonViewModel,
    checkOutViewModel: CheckOutViewModel
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = Unit){
        val items = viewModel.state.value.cartItems.joinToString(separator = "#") { "${it.id}" }
        val address = viewModel.state.value.addresses.firstOrNull { it.isDefault }
        val token = InstallationUnique.id(context)
        checkOutViewModel.setEvent(
            CheckOutContract.CheckOutEvent.OnEditOrderDetails(
            OrderDetails(
                price = viewModel.state.value.cartItems.sumOf { it.price*it.qty },
                address = "${address?.village ?: ""}, ${address?.street ?: ""}, ${address?.parish ?: ""}, ${address?.division ?: ""}, ${address?.district ?: ""}",
                contact = address?.phoneNumber ?: "",
                identification = token ?: "",
                status = "Placed",
                items = items,
                contactName = address?.contactName ?: "",
            )
        ))
    }
    LazyColumn(modifier = modifier
        .fillMaxSize()
        .padding(8.dp)){
        item {
            SectionHeader(header = "Order is to be delivered to: ")
            Spacer(modifier = Modifier.height(8.dp))
            AddressRow(viewModel = checkOutViewModel)
            Spacer(modifier = Modifier.height(16.dp))
            SectionHeader(header = "Order Summary")
            viewModel.state.value.cartItems.forEach { item->
                SummaryItem(cartItem = item)
            }
        }
    }
}


@Composable
fun AddressRow(modifier: Modifier = Modifier.fillMaxWidth(), viewModel: CheckOutViewModel) {
    val width = LocalConfiguration.current.screenWidthDp
    Row(modifier = modifier.padding(horizontal = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        AddButton(){ viewModel.setEvent(CheckOutContract.CheckOutEvent.OnToggleAddressDialog(true, true))}
        Spacer(modifier = Modifier.width(16.dp))
        if (viewModel.state.value.addresses.isEmpty()){
            Surface(modifier = modifier
                .fillMaxWidth(0.8f)
                .height(150.dp)
                .padding(8.dp), shape = RoundedCornerShape(8.dp), elevation = 2.dp) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "No Address found Please Click The Add Button On The Left To Add Delivery Information",
                        style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold),
                        maxLines = 2,
                        modifier = Modifier.fillMaxWidth(0.8f), overflow = TextOverflow.Ellipsis)
                }
            }
        }else{
            LazyRow(modifier = Modifier.fillMaxWidth()){
                items(viewModel.state.value.addresses){address->
                    AddressCard(address = address, modifier = Modifier
                        .width((width * 0.5).dp)
                        .clickable {
                            viewModel.setEvent(
                                CheckOutContract.CheckOutEvent.ChangeDefaultAddress(
                                    address
                                )
                            )
                        }, onEdit = {
                        viewModel.setEvent(CheckOutContract.CheckOutEvent.OnEditAddress(address))
                    }){
                        viewModel.setEvent(CheckOutContract.CheckOutEvent.OnDeleteAddress(address))
                    }
                }
            }
        }
    }
    if (viewModel.state.value.openAddressDialog)
        CustomDialog(
            onNegativeClick = { viewModel.setEvent(CheckOutContract.CheckOutEvent.OnToggleAddressDialog(false,false)) }) {
            DeliveryInfo(modifier = Modifier, borrowNumber = {
                viewModel.setEvent(CheckOutContract.CheckOutEvent.OnAddNumber(it))
            }, address = viewModel.state.value.addressOnEdit){ viewModel.setEvent(CheckOutContract.CheckOutEvent.OnToggleAddressDialog(false, false)) }
        }
}


@Composable
fun AddressCard(modifier: Modifier = Modifier, address:Address, onEdit:()->Unit, onDelete:()->Unit) {
    var street = address.street
//    if (address.nearbyPlace.isNotEmpty())
//        street+=" near by ${address.nearbyPlace}"
    val textColor = if (address.isDefault) Color.White else lightBack
    val containColor = if (address.isDefault) lightBlue else Color.White
    Surface(modifier = modifier
        .height(150.dp)
        .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = if (address.isDefault) 0.dp else 2.dp,
        color = containColor
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Row(modifier = Modifier.align(Alignment.TopEnd)) {
                TextButton(onClick = { onEdit() }) {
                    Text(text = "Edit", style = MaterialTheme.typography.caption, color = textColor)
                }
                if (!address.isDefault)
                    TextButton(onClick = { onDelete() }) {
                        Text(text = "Delete", style = MaterialTheme.typography.caption, color = textColor)
                    }
            }

            Column(modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(horizontal = 16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 16.dp)) {
                    Icon(painter = painterResource(id = R.drawable.person), contentDescription = "", modifier = Modifier
                        .padding(end = 8.dp)
                        .size(30.dp), tint = if (address.isDefault) Color.White else lightBlue)
                    Column {
                        Text(text = address.contactName, color = textColor, modifier = Modifier.fillMaxWidth(0.7f), overflow = TextOverflow.Ellipsis, maxLines = 1)
                        Text(text = address.phoneNumber, color = textColor, modifier = Modifier.fillMaxWidth(0.7f), overflow = TextOverflow.Ellipsis, maxLines = 1)
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(painter = painterResource(id = R.drawable.location), contentDescription = "", modifier = Modifier
                        .padding(end = 16.dp)
                        .size(30.dp), tint = Color.Red)
                    Column() {
                        Text(text = address.village, style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.ExtraBold, color = textColor),modifier = Modifier.fillMaxWidth(0.7f), overflow = TextOverflow.Ellipsis, maxLines = 1)
                        Text(text = address.street, style = MaterialTheme.typography.caption.copy(color = textColor), modifier = Modifier.fillMaxWidth(0.7f), overflow = TextOverflow.Ellipsis, maxLines = 1)
                    }
                }
            }
        }
    }
}

@Composable
fun SummaryItem(modifier: Modifier = Modifier, cartItem:CartItems) {
    val image = getImage(url = cartItem.image, onSuccess = {}){}
    Column(modifier = modifier
        .fillMaxWidth()
        .padding(8.dp)) {
        Divider(color = Color.White, thickness = 3.dp)
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth(0.5f)) {
                Surface(modifier = Modifier.size(50.dp), shape = RoundedCornerShape(8.dp), color = Color.Transparent) {
                    Box(modifier = Modifier.padding(4.dp)) {
                        Image(painter = image, contentDescription = "", modifier =  Modifier.fillMaxSize())
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = cartItem.name, style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.ExtraBold), maxLines = 2, overflow = TextOverflow.Ellipsis)
            }
            val money = (cartItem.price*cartItem.qty).toInt().comma()
            Text(text = "Qty:    ${cartItem.qty}", modifier = Modifier.fillMaxWidth(0.4f))
            Text(text = "Shs.$money/=", modifier = Modifier.fillMaxWidth(0.6f))

        }
    }
}
