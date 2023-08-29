package com.target.supermarket.presentation.checkout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.target.supermarket.presentation.cart.TextButtonCustom

@Composable
fun CustomDialog(onNegativeClick:()->Unit, content:@Composable ()->Unit) {
    Dialog(onDismissRequest = { onNegativeClick() }, properties = DialogProperties(
        dismissOnClickOutside = false
    )) {
        Surface(
            elevation = 4.dp,
            shape = RoundedCornerShape(16.dp)
        ) {
            content()
        }
    }
}