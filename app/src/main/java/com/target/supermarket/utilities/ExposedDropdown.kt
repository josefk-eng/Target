package com.target.supermarket.utilities

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.toSize
import com.target.supermarket.ui.theme.lightBlue
import com.target.supermarket.ui.theme.lighterGray
import kotlin.math.exp

@Composable
fun ExposedDropdown(
    items: List<Pair<String, Boolean>>,
    onSelected: (String) -> Unit,
    label:String,
    value: String,
    keyboardActions: KeyboardActions,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        imeAction = ImeAction.Next
    ),
    isError:Boolean = false,
    onFocusChanged:()->Unit
) {
    var expanded by remember { mutableStateOf(false) }
//    var selectedText by remember { mutableStateOf("") }

    var textfieldSize by remember { mutableStateOf(Size.Zero)}

    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp //it requires androidx.compose.material:material-icons-extended
    else
        Icons.Filled.KeyboardArrowDown


    Box(modifier = Modifier.background(lighterGray)) {
        TextField(
            value = value,
            onValueChange = { onSelected(it) },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    //This value is used to assign to the DropDown the same width
                    textfieldSize = coordinates.size.toSize()
                }.onFocusChanged { onFocusChanged() }
                .clickable { expanded = true },
            label = {Text(label)},
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent
            ),
            trailingIcon = {
                Icon(icon,"contentDescription",
                    Modifier.clickable { expanded = !expanded })
            },
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = true,
            maxLines = 1,
            readOnly = true,
            isError = isError
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current){textfieldSize.width.toDp()})
        ) {
            items.forEach { label ->
                DropdownMenuItem(onClick = {
                    onSelected(label.first)
                    expanded = false
                }, enabled = label.second) {
                    Text(text = label.first)
                }
            }
        }
    }
}


@Preview
@Composable
fun CheckOut() {
//    ExposedDropdown(listOf(Pair("Item 1", true),Pair("Item 2", false), Pair("Item 3", true)), onSelected = {},"District","Selected"){}
}