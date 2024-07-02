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
import androidx.compose.runtime.LaunchedEffect
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
import com.example.trackerapp.domain.model.mapModels.deviceList.VehicleList
import com.example.trackerapp.domain.model.mapModels.firmList.FirmList
import com.example.trackerapp.presentation.map.MapViewModel
import com.example.trackerapp.ui.theme.PrimaryGreen
import com.example.trackerapp.ui.theme.PrimaryOrange
import com.example.trackerapp.util.LoadingScreen
import com.example.trackerapp.util.Response
import kotlinx.coroutines.withTimeout

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    authViewModel: AuthViewModel = hiltViewModel(),
    mapViewModel: MapViewModel = hiltViewModel()
) {

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp

    var isLoading by remember { mutableStateOf(false) }

    var firmList by remember { mutableStateOf<List<FirmList>>(emptyList()) }

    var deviceList by remember { mutableStateOf<List<VehicleList>>(emptyList()) }

    var selectedFirmInfo by remember {
        mutableStateOf(FirmList("", "", ""))
    }

    var selectedDeviceInfo by remember {
        mutableStateOf(VehicleList("", ""))
    }

    var name by remember {
        mutableStateOf(TextFieldValue(""))
    }
    var selectedFirm by remember {
        mutableStateOf(TextFieldValue(""))
    }
    var selectedVehicle by remember {
        mutableStateOf(TextFieldValue(""))
    }

    var errorMessage by remember {
        mutableStateOf("")
    }

    var showLoadingScreen by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = "") {
        showLoadingScreen = true
        try {
            withTimeout(5000) { // 5 seconds timeout
                authViewModel.getFirmList {
                    when (it) {
                        is Response.Success -> {
                            showLoadingScreen = false
                            firmList = it.data.FirmList.map {
                                FirmList(
                                    firmName = it.firmName,
                                    appid = it.appid,
                                    signature = it.signature
                                )
                            }
                        }

                        is Response.Error -> {
                            errorMessage = "Server down. Unable to fetch firm list"
                        }
                    }
                }
            }
        } catch (e: Exception) {
            errorMessage = "E: Server down. Unable to fetch firm list"
        }
    }

    fun getDeviceList(firmName: String, retryCount: Int = 2) {
        errorMessage = ""
        selectedVehicle = TextFieldValue("")
        deviceList = emptyList()
        showLoadingScreen = true

        selectedFirmInfo = firmList.find { it.firmName === firmName }!!

        mapViewModel.generateAccessToken(
            appId = selectedFirmInfo.appid,
            signature = selectedFirmInfo.signature,
            callBack = {
                when (it) {
                    true -> {
                        mapViewModel.getDeviceList {
                            when (it) {
                                is Response.Success -> {
                                    showLoadingScreen = false
                                    deviceList = it.data.data.map {
                                        VehicleList(
                                            vehicleNumber = it.deviceName,
                                            imei = it.imei
                                        )
                                    }
                                }

                                is Response.Error -> {
                                    if (retryCount > 0) {
                                        getDeviceList(firmName, retryCount - 1)
                                    } else {
                                        showLoadingScreen = false
                                        errorMessage = it.error
                                    }
                                }
                            }
                        }
                    }

                    false -> {
                        showLoadingScreen = false
                        errorMessage = "Unable to fetch vehicle list"
                    }
                }
            }
        )

    }

    fun getSelectedVehicle(vehicleNumber: String) {
        selectedDeviceInfo = deviceList.find { it.vehicleNumber === vehicleNumber }!!
    }

    fun onActionButtonClick() {
        errorMessage = ""

        if (name.text.isBlank() || selectedFirm.text.isBlank() || selectedVehicle.text.isBlank()) {
            errorMessage = "Fields can't be empty"
        } else {
            isLoading = true

            authViewModel.onRegisterUser(
                name = name.text,
                firmName = selectedFirm.text,
                appId = selectedFirmInfo.appid,
                signature = selectedFirmInfo.signature,
                vehicleNumber = selectedVehicle.text,
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
                    value = selectedFirm.text,
                    onValueChange = {
                        selectedFirm = TextFieldValue(it)
                        getDeviceList(it)
                    },
                    options = firmList.map { it.firmName }
                )

                DropDownList(
                    label = "Select Vehicle",
                    value = selectedVehicle.text,
                    onValueChange = {
                        selectedVehicle = TextFieldValue(it)
                        getSelectedVehicle(it)
                    },
                    options = deviceList.map { it.vehicleNumber }
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
    if (showLoadingScreen) {
        Box(modifier = Modifier.fillMaxSize()) { LoadingScreen() }
    }
}