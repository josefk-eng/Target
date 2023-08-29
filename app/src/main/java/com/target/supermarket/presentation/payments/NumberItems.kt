package com.target.supermarket.presentation.payments

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.target.supermarket.R
import com.target.supermarket.presentation.cart.TextButtonCustom
import com.target.supermarket.presentation.commons.SearchTextInputBordered
import com.target.supermarket.presentation.home.deals.AddButton
import com.target.supermarket.presentation.viewModels.CheckOutContract
import com.target.supermarket.presentation.viewModels.CheckOutViewModel
import com.target.supermarket.ui.theme.lightBlue
import com.target.supermarket.ui.theme.lighterGray
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun NumberItem(modifier: Modifier = Modifier, number:String, isActive:Boolean, onSelect:()->Unit, onDelete:()->Unit) {
    val color = if (isActive) Color.White else lightBlue
    val bgColor = if (isActive) lightBlue else Color.White
    Box() {
        Card(shape = RoundedCornerShape(16.dp),modifier = modifier, backgroundColor = bgColor, elevation = 0.dp,) {
            Row(
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 16.dp)
                    .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = number,
                        style = MaterialTheme.typography.caption.copy(
                            fontSize = 18.sp,
                            color = color,
                            fontWeight = if (isActive) FontWeight.ExtraBold else FontWeight.Bold
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    if (!isActive)
                        TextButtonCustom(title = "Select") {
                            onSelect()
                        }
                }
                Image(painter = painterResource(id = getID(number)), contentDescription = "")
            }
        }
        if (!isActive)
            AddButton(icon = painterResource(id = R.drawable.dash), modifier = Modifier.align(
                Alignment.BottomStart)) {
                onDelete()
            }
    }
}

fun getID(number: String): Int {
    return if (
        number.startsWith("070") ||
        number.startsWith("075")
    ){
        R.drawable.airtellogo
    }else if (
        number.startsWith("077") ||
        number.startsWith("078")  ||
        number.startsWith("076")
    ){
        R.drawable.mtnlogo
    }else 0
}

@Composable
fun AddNumber(modifier: Modifier = Modifier, scope: CoroutineScope, viewModel: CheckOutViewModel, state: ScaffoldState) {
    Card(shape = RoundedCornerShape(16.dp),modifier = modifier, elevation = 0.dp) {
        Row(modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            SearchTextInputBordered(modifier = Modifier
                .padding(vertical = 4.dp, horizontal = 12.dp)
                .weight(7f), title = "Add New Mobile Money Number", error = viewModel.state.value.NumberError, value = viewModel.state.value.newNumber){
                if (it.length<=10) {
                    viewModel.setEvent(CheckOutContract.CheckOutEvent.OnNumberChanged(it))
                }
            }
            AddButton() {
                viewModel.setEvent(CheckOutContract.CheckOutEvent.CheckForNewNumberErrors{
                    if (it==null){
                        viewModel.setEvent(CheckOutContract.CheckOutEvent.OnAddNumber(null))
                    }else{
                        scope.launch {
                            state.snackbarHostState.showSnackbar(it.message ?: "An error has occurred")
                        }
                    }
                })
            }
        }
    }
}


@Preview
@Composable
fun Preview() {
//    NumberItem(number = "+256 704926930", isActive = true)
//    AddNumber(number = "Add Number")
}