package com.target.supermarket.presentation.accounts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.target.supermarket.presentation.viewModels.CommonContract
import com.target.supermarket.presentation.viewModels.CommonViewModel

@Composable
fun Login(modifier: Modifier = Modifier, viewModel: CommonViewModel) {
    var email by rememberSaveable{ mutableStateOf("") }
    var password by rememberSaveable{ mutableStateOf("") }
    Box {
        IconButton(onClick = { viewModel.setEvent(CommonContract.CommonEvent.OpenAccDialog(false)) }, modifier = Modifier.align(Alignment.TopEnd)) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "")
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier
                .wrapContentSize()
                .padding(horizontal = 16.dp, vertical = 32.dp)
        ) {
            Text(text = "Personalise Your Shopping", style = MaterialTheme.typography.h6)
            Spacer(modifier = Modifier.height(20.dp))

            CredentialInput(
                modifier = Modifier.fillMaxWidth(),
                label = "Enter email/username",
                value = email,
                error = ""
            ) {
                email = it
            }
            Spacer(modifier = Modifier.height(16.dp))
            CredentialInput(
                modifier = Modifier.fillMaxWidth(),
                label = "Enter your password",
                value = password,
                error = ""
            ) {
                password = it
            }
            Box(modifier = Modifier.fillMaxWidth().padding(top = 4.dp), contentAlignment = Alignment.CenterEnd) {
                TextButton(onClick = { /*TODO*/ }) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = "")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Continue")
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "OR", style = MaterialTheme.typography.h6)
            Spacer(modifier = Modifier.height(8.dp))
            SocialButton(
                social = Social.Google,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable { })
            SocialButton(
                social = Social.Facebook,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable { })
        }
    }
}