package com.example.trackerapp.presentation.auth

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.trackerapp.presentation.home.HomeViewModel
import com.example.trackerapp.ui.theme.PrimaryGreen
import com.example.trackerapp.ui.theme.PrimaryOrange
import com.example.trackerapp.util.Response

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel = hiltViewModel(),
    homeViewModel: HomeViewModel = hiltViewModel(),
    onRegistrationRedirect: () -> Unit,
    onHomeRedirect: () -> Unit
) {

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val context = LocalContext.current

    var isLoading by remember { mutableStateOf(false) }

    var buttonLabel by remember { mutableStateOf("Get OTP") }

    var otpSent by remember {
        mutableStateOf(false)
    }

    var number by remember {
        mutableStateOf(TextFieldValue(""))
    }
    var otp by remember {
        mutableStateOf(TextFieldValue(""))
    }

    var errorMessage by remember {
        mutableStateOf("")
    }

    var otpResponse by remember {
        mutableStateOf("")
    }

    fun onSuccessfulVerify() {
        homeViewModel.getUserInfo {
            when (it) {
                true -> {
                    onHomeRedirect()
                }

                false -> {
                    errorMessage = "Something went wrong. Please click login again"
                }
            }
        }
    }

    LaunchedEffect(otpSent) {
        buttonLabel = if (otpSent) "Login" else "GET OTP"
    }

    fun onActionButtonClick() {
        errorMessage = ""

        if (!otpSent) {
            if (number.text.isBlank()) {
                errorMessage = "Number can't be empty"
            } else if (number.text.length < 10) {
                errorMessage = "Please enter a valid 10 digit number"
            } else {
                isLoading = true
                authViewModel.onSendOTP(
                    number = number.text,
                    callback = {
                        isLoading = false

                        when (it) {
                            is Response.Success -> {
                                errorMessage = ""
                                otpResponse = it.data.message
                                otpSent = true
                                Toast.makeText(context, "OTP sent successfully", Toast.LENGTH_LONG)
                                    .show()
                            }

                            is Response.Error -> {
                                errorMessage = it.error
                            }
                        }
                    }
                )
            }
        } else {
            if (otp.text.isBlank()) {
                errorMessage = "Otp can't be empty"
            } else {
                isLoading = true
                authViewModel.onVerifyOTP(
                    number = number.text,
                    otp = otp.text,
                    callback = {
                        isLoading = false

                        when (it) {
                            is Response.Success -> {
                                errorMessage = ""
                                if (otpResponse == "OTP sent for login successfully") onSuccessfulVerify()
                                else onRegistrationRedirect()
                            }

                            is Response.Error -> {
                                errorMessage = it.error
                            }
                        }
                    }
                )
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryGreen),
    ) {

        Canvas(modifier = Modifier.align(Alignment.TopEnd)) {
            drawCircle(radius = screenWidth.dp.toPx() / 2, color = PrimaryOrange)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(30.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                HeaderText(text = "Login")
            }

            Spacer(modifier = Modifier.height(100.dp))

            InputField(
                label = "Phone Number",
                value = number,
                onValueChange = {
                    if (it.text.length <= 10) {
                        number = it
                    }
                },
                keyboardType = KeyboardType.Number
            )

            Spacer(modifier = Modifier.height(30.dp))

            if (otpSent) {
                InputField(
                    label = "OTP",
                    value = otp,
                    onValueChange = {
                        if (it.text.length <= 4) {
                            otp = it
                        }
                    },
                    keyboardType = KeyboardType.Number
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            ErrorField(errorText = errorMessage)

            Spacer(modifier = Modifier.height(20.dp))

            ActionButton(
                text = buttonLabel,
                isLoading = isLoading,
                onClick = { onActionButtonClick() })

        }
    }
}