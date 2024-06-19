package com.example.trackerapp.presentation.home

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleDropDownList(
    value: String,
    onValueChange: (String) -> Unit,
    options: List<String>
) {
    var expended by remember {
        mutableStateOf(false)
    }

    ExposedDropdownMenuBox(
        expanded = expended,
        onExpandedChange = { expended = !expended }
    ) {
        InputField(
            modifier = Modifier.menuAnchor(),
            value = TextFieldValue(value),
            expended = expended
        )

        DropdownMenu(
            expanded = expended,
            onDismissRequest = { expended = false },
            modifier = Modifier.fillMaxWidth(.8f)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(text = option) },

                    onClick = {
                        expended = !expended
                        onValueChange(option)
                    },

                    colors = MenuDefaults.itemColors(textColor = Color.White)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InputField(
    modifier: Modifier = Modifier,
    value: TextFieldValue,
    expended: Boolean = false
) {
    TextField(
        value = value,

        onValueChange = {},

        singleLine = true,

        readOnly = true,

        modifier = modifier.fillMaxWidth(),

        textStyle = TextStyle(
            fontSize = 18.sp,
            fontWeight = FontWeight.Normal
        ),

        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            unfocusedLabelColor = Color.LightGray,
            focusedLabelColor = Color.White,
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent
        ),

        trailingIcon = {
            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expended)
        }
    )
}
