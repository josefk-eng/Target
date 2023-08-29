package com.target.supermarket.presentation.payments

import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.target.supermarket.presentation.viewModels.CheckOutContract
import com.target.supermarket.presentation.viewModels.CheckOutViewModel
import kotlinx.coroutines.CoroutineScope

@Composable
fun MobileMoneyDetails(modifier: Modifier = Modifier, viewModel: CheckOutViewModel, state: ScaffoldState, scope: CoroutineScope) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        item {
            Text(text = "Transaction will be carried out on: ", style = MaterialTheme.typography.h6.copy(fontSize = 18.sp, fontWeight = FontWeight.Bold), modifier = Modifier.padding(bottom = 8.dp, top = 16.dp))
        }
        items(viewModel.state.value.numbers){number->
            NumberItem(number = number.number, isActive = number.isActive, modifier = Modifier
                .padding(vertical = 6.dp),
                onSelect = {viewModel.setEvent(CheckOutContract.CheckOutEvent.OnActiveStatusChanged(number))}){
                viewModel.setEvent(CheckOutContract.CheckOutEvent.OnDeleteNumber(number))
            }
        }
        item{
            AddNumber(viewModel = viewModel, modifier = Modifier, state = state, scope = scope)
        }
    }
}