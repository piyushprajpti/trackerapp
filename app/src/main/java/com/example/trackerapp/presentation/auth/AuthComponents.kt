package com.example.trackerapp.presentation.auth

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trackerapp.ui.theme.ErrorRed
import com.example.trackerapp.ui.theme.PrimaryOrange
import com.example.trackerapp.ui.theme.Typography

@Composable
fun HeaderText(
    text: String
) {
    Text(
        text = text,
        style = Typography.titleLarge,
        color = Color.White,
        textAlign = TextAlign.End
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputField(
    modifier: Modifier = Modifier,
    label: String,
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    readOnly: Boolean = false,
    trailingIcon: Boolean = false,
    expended: Boolean = false
) {
    TextField(
        label = { Text(text = label) },

        value = value,

        onValueChange = { onValueChange(it) },

        singleLine = true,

        readOnly = readOnly,

        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),

        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            unfocusedLabelColor = Color.LightGray,
            focusedLabelColor = Color.White,
            unfocusedIndicatorColor = Color.LightGray,
            focusedIndicatorColor = Color.White
        ),

        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),

        trailingIcon = {
            if (trailingIcon) ExposedDropdownMenuDefaults.TrailingIcon(expanded = expended)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownList(
    label: String,
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
            label = label,
            value = TextFieldValue(value),
            onValueChange = {},
            readOnly = true,
            trailingIcon = true,
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

@Composable
fun ActionButton(
    text: String,
    isLoading: Boolean,
    onClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Restart
        ),
        label = ""
    )


    TextButton(
        onClick = { onClick() },
        shape = RoundedCornerShape(5.dp),
        colors = ButtonDefaults.textButtonColors(containerColor = PrimaryOrange),
        modifier = Modifier
            .fillMaxWidth()

    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .rotate(rotation)
                    .padding(vertical = 5.dp)
                    .size(26.dp),
                strokeWidth = 2.dp,
                color = Color.White
            )
        } else {
            Text(
                text = text,
                style = Typography.titleSmall,
                color = Color.White,
                modifier = Modifier.padding(vertical = 5.dp)
            )
        }
    }
}

@Composable
fun ErrorField(
    errorText: String
) {
    Text(
        text = errorText,
        modifier = Modifier
            .fillMaxWidth(),
        color = ErrorRed,
        fontSize = 14.sp
    )
}