package com.target.supermarket.presentation.feature

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.target.supermarket.R

@Composable
fun ActionButtons(modifier: Modifier, qty:Int, onRemove:()->Unit, onAdd:()->Unit) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        if (qty>0){
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                ButtonWithRoundCorners(icon = painterResource(id = R.drawable.dash), text = null){
                    onRemove()
                }
                Text(text = "$qty")
                ButtonWithRoundCorners(icon = painterResource(id = R.drawable.plus),text = null) {
                    onAdd()
                }
            }
        }else{
            ButtonWithRoundCorners(icon = null, text = "Add") {onAdd()}
        }
    }
}


@Preview
@Composable
fun TestPrev() {
//    ActionButtons()
}


