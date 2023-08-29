package com.target.supermarket.presentation.dialogs

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun AccountsDialog(onDismiss:()->Unit,content:@Composable ()->Unit) {
    Dialog(onDismissRequest = {onDismiss()}) {
        Surface(
            elevation = 4.dp,
            shape = RoundedCornerShape(16.dp)
        ) {
            content()
        }
    }
}