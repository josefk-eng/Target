package com.target.supermarket.presentation.accounts

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.target.supermarket.R

@Composable
fun CredentialInput(
    modifier: Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Text,
        imeAction = ImeAction.Next
    ),
    keyboardActions: KeyboardActions = KeyboardActions(onDone = {}, onNext = {}),
    label:String,
    value:String,
    error:String,
    onValueChange:(String)->Unit
) {
    TextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        keyboardActions = keyboardActions,
        keyboardOptions = keyboardOptions,
        label = { Text(text = label)},
        isError = error.isNotEmpty(),
        singleLine = true,
        maxLines = 1,
        colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent)
    )
}

enum class Social{
    Facebook,
    Google
}

@Composable
fun SocialButton(
    social: Social,modifier: Modifier
) {
    val color = when(social){
        Social.Facebook -> Color.Blue.copy(alpha = 0.4f)
        Social.Google -> Color.Red.copy(alpha = 0.4f)
    }
    val image = when(social){
        Social.Facebook -> painterResource(id = R.drawable.facebook)
        Social.Google -> painterResource(id = R.drawable.google)
    }
    Surface(shape = RoundedCornerShape(30.dp), color = color, modifier = modifier) {
        Box(modifier = Modifier) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Card(shape = CircleShape, modifier = Modifier
                    .size(40.dp)) {
                    Image(painter = image, contentDescription = "", contentScale = ContentScale.FillBounds, modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = when(social){
                        Social.Facebook -> "Sign In With Facebook"
                        Social.Google -> "Sign In With Google"
                    },
                    style = MaterialTheme.typography.caption.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    ),
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
    }
}