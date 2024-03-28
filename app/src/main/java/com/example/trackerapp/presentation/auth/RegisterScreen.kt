package com.example.trackerapp.presentation.auth

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.trackerapp.ui.theme.PrimaryGreen
import com.example.trackerapp.ui.theme.PrimaryOrange
import com.example.trackerapp.util.Response

@Composable
fun RegisterScreen(
    number: String?,
    onRegisterSuccess: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp

    var isLoading by remember { mutableStateOf(false) }

    var name by remember {
        mutableStateOf(TextFieldValue(""))
    }
    var firm by remember {
        mutableStateOf(TextFieldValue(""))
    }
    var vehicleNumber by remember {
        mutableStateOf(TextFieldValue(""))
    }

    var errorMessage by remember {
        mutableStateOf("")
    }

    fun onActionButtonClick() {
        isLoading = true
        errorMessage = ""

        viewModel.onRegisterUser(
            number = number.toString(),
            name = name.text,
            firmName = firm.text,
            vehicleNumber = vehicleNumber.text,
            callback = {
                isLoading = false

                when (it) {
                    is Response.Success -> {
                        errorMessage = ""
                        onRegisterSuccess()
                    }

                    is Response.Error -> {
                        errorMessage = it.error
                    }
                }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryGreen)
    ) {
        Canvas(modifier = Modifier.align(Alignment.TopEnd)) {
            drawCircle(radius = screenWidth.dp.toPx() / 2, color = PrimaryOrange)
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(30.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    HeaderText(text = "Register")
                }
            }

            // input fields
            item {
                Spacer(modifier = Modifier.height(20.dp))

                InputField(
                    label = "Name",
                    value = name,
                    onValueChange = { name = it },
                    keyboardType = KeyboardType.Text
                )

                DropDownList(
                    label = "Select Firm",
                    value = firm.text,
                    onValueChange = { firm = TextFieldValue(it) },
                    options = listOf("TCS", "IBM", "Google", "Microsoft")
                )

                DropDownList(
                    label = "Select Vehicle",
                    value = vehicleNumber.text,
                    onValueChange = { vehicleNumber = TextFieldValue(it) },
                    options = listOf(
                        "RJ 02 CE 0807",
                        "DL 4A CV 2156",
                        "DL 3C TA 9412",
                        "HR 55 X 2822"
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

            }

            item {
                ErrorField(errorText = errorMessage)

                Spacer(modifier = Modifier.height(20.dp))
            }

            item {
                ActionButton(
                    text = "Register",
                    isLoading = isLoading,
                    onClick = { onActionButtonClick() })
            }
        }
    }
}

