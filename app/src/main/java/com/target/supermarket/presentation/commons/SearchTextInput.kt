package com.target.supermarket.presentation.commons

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.target.supermarket.ui.theme.baseColor
import com.target.supermarket.ui.theme.lighterGray

@Composable
fun SearchTextInput(modifier: Modifier, title:String = "Products",value:String, onValueChanged:(String)->Unit) {
    Box(
        modifier = modifier
            .wrapContentWidth()
            .background(
                color = lighterGray,
                shape = RoundedCornerShape(16.dp)
            )
            .height(45.dp)
            .padding(8.dp),
        contentAlignment = Alignment.CenterStart
    ) {
//        Row(Modifier.wrapContentWidth(), verticalAlignment = Alignment.CenterVertically) {
//            Dropdown(
//                modifier = Modifier,
//                items = listOf("All Categories","Category1","Category2","Category3"),
//            )
        FormInput(modifier = Modifier.wrapContentWidth(), value = value, label = "Search $title...", onValueChange = onValueChanged)
//        }
    }
}

@Composable
fun SearchTextInputBordered(modifier: Modifier, value: String, title:String = "Products", error:String, onValueChange: (String) -> Unit) {
    Column(modifier = modifier
        .fillMaxWidth()
        .wrapContentHeight()) {
        Box(
            modifier = Modifier
                .wrapContentWidth()
                .background(
                    color = lighterGray,
                    shape = RoundedCornerShape(16.dp)
                )
                .border(1.dp, baseColor, shape = RoundedCornerShape(16.dp))
                .height(45.dp)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            FormInput(modifier = Modifier.wrapContentWidth(), value = value, label = title, onValueChange = onValueChange, inputType = KeyboardOptions(keyboardType = KeyboardType.Phone))
        }
        if (error.isNotEmpty())
            Text(text = error, style = MaterialTheme.typography.caption.copy(color = Color.Red))
    }
}

@Composable
fun FormInput(modifier: Modifier = Modifier
    .padding(start = 32.dp)
    .fillMaxSize(), value:String,label:String,
              inputType:KeyboardOptions = KeyboardOptions(
                  keyboardType = KeyboardType.Text,
                  imeAction = ImeAction.Done
              ),
              onValueChange:(String)->Unit) {
    Box(modifier = modifier, contentAlignment = Alignment.CenterStart) {
        BasicTextField(value = value, onValueChange = onValueChange, modifier = Modifier.fillMaxWidth(), keyboardOptions = inputType, keyboardActions = KeyboardActions(

        ), singleLine = true, maxLines = 1)
        if (value.isEmpty())
            Text(text = label, color = Color.LightGray)
    }
}

@Composable
fun TransparentInput(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    keyboardActions: KeyboardActions,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        imeAction = ImeAction.Next
    ),
    isError:Boolean = false,
    onValueChange: (String) -> Unit
) {
    TextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent
        ),
        label = { Text(text = label) },
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = true,
        maxLines = 1,
        isError = isError
    )
}