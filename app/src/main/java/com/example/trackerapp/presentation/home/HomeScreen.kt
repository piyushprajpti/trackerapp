package com.example.trackerapp.presentation.home

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.trackerapp.R
import com.example.trackerapp.domain.model.mapModels.deviceList.VehicleList
import com.example.trackerapp.domain.model.mapModels.historyPlayback.PlaybackLatLngList
import com.example.trackerapp.presentation.map.MapViewModel
import com.example.trackerapp.presentation.map.MapViewer
import com.example.trackerapp.util.LoadingScreen
import com.example.trackerapp.util.Response
import com.mappls.sdk.maps.annotations.IconFactory
import kotlinx.coroutines.delay
import java.util.Calendar

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    mapViewModel: MapViewModel = hiltViewModel(),
    authRedirect: () -> Unit
) {
    val drawerState = remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current

    val carIcon = remember {
        IconFactory.getInstance(context).fromResource(R.drawable.car2)
    }

    var vehicleList by remember {
        mutableStateOf<List<VehicleList>>(emptyList())
    }

    val selectedVehicle = remember {
        mutableStateOf(TextFieldValue(""))
    }

    val toggle = remember {
        mutableStateOf(false)
    }

    var showLoadingScreen by remember {
        mutableStateOf(false)
    }

    val lat = remember {
        mutableStateOf("28.6185149")
    }
    val lng = remember {
        mutableStateOf("77.2141112")
    }

    val selectedScreen = remember {
        mutableStateOf(1)
    }

    val isPlaybackStarted = remember {
        mutableStateOf(false)
    }


    val activeSpeedButton = remember {
        mutableStateOf(1)
    }

    val playbackLatLngList = remember {
        mutableStateOf<List<PlaybackLatLngList>>(emptyList())
    }

    fun createPolyline(startTime: Long? = null, endTime: Long? = null) {

        val currentStartTime: Long
        val currentEndTime: Long

        if (startTime == null || endTime == null) {

            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)

            currentEndTime = calendar.timeInMillis / 1000

            calendar.add(Calendar.DAY_OF_YEAR, -1)
            currentStartTime = calendar.timeInMillis / 1000
        } else {
            currentStartTime = startTime
            currentEndTime = endTime
        }

        mapViewModel.getHistoryPlayback(
            imei = vehicleList.find { selectedVehicle.value.text == it.vehicleNumber }?.imei
                ?: "",
            startTime = currentStartTime,
            endTime = currentEndTime,
            callBack = {
                when (it) {
                    is Response.Success -> {
                        if (it.data.playbackLatLngList.isEmpty()) {
                            createPolyline(
                                startTime = currentStartTime - 86400,
                                endTime = currentEndTime - 86400
                            )
                        } else {
                            playbackLatLngList.value = it.data.playbackLatLngList.map {
                                PlaybackLatLngList(
                                    code = it.code,
                                    imei = it.imei,
                                    lat = it.lat,
                                    lng = it.lng,
                                    speed = it.speed,
                                    course = it.course,
                                    gpsTime = it.gpsTime,
                                    positionType = it.positionType
                                )
                            }
                        }
                    }

                    is Response.Error -> {
                        Log.d("TAG", "createPolyline: ${it.error}")
                        Toast.makeText(
                            context,
                            "Unable to draw polyline",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        )
    }

    fun getDeviceList(appid: String?, signature: String?) {
        showLoadingScreen = true

        mapViewModel.generateAccessToken(
            appId = appid.toString(),
            signature = signature.toString(),
            callBack = {
                when (it) {
                    true -> {
                        mapViewModel.getDeviceList {
                            when (it) {
                                is Response.Success -> {
                                    showLoadingScreen = false
                                    vehicleList = it.data.data.map {
                                        VehicleList(
                                            vehicleNumber = it.deviceName,
                                            imei = it.imei
                                        )
                                    }
                                    it.data.data.find { selectedVehicle.value.text == it.deviceName }
                                        ?.let { it1 ->
                                            mapViewModel.setValueInPref(
                                                "imei",
                                                it1.imei
                                            )
                                        }
                                    createPolyline()
                                }

                                is Response.Error -> {
                                    showLoadingScreen = false
                                    Toast.makeText(
                                        context,
                                        "Unable to fetch vehicle list",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        }
                    }

                    false -> {
                        showLoadingScreen = false
                        Toast.makeText(context, "Server down", Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
        )
    }

    fun getLiveLocation() {
        mapViewModel.getLiveLocation {
            when (it) {

                is Response.Success -> {
                    lat.value = it.data.lat
                    lng.value = it.data.lng
                    mapViewModel.setValueInPref("lat", it.data.lat)
                    mapViewModel.setValueInPref("lng", it.data.lng)
                }

                is Response.Error -> {

                }
            }
        }
    }

    val name = remember {
        mutableStateOf("")
    }

    val phoneNumber = remember {
        mutableStateOf("")
    }

    val firmName = remember {
        mutableStateOf("")
    }

    val currentSpeed = remember {
        mutableStateOf(0)
    }

    fun onVehicleChange(vehicleNumber: String) {
        val imei = vehicleList.find { vehicleNumber == it.vehicleNumber }?.imei ?: ""
        mapViewModel.setValueInPref("imei", imei)
    }

    fun onLogoutClick() {
        mapViewModel.setValueInPref("appid", "")
        mapViewModel.setValueInPref("signature", "")
        mapViewModel.setValueInPref("imei", "")
        authRedirect()
    }

    LaunchedEffect(Unit) {

        val appId = mapViewModel.getValueFromPref("appId")
        val signature = mapViewModel.getValueFromPref("signature")
        selectedVehicle.value =
            TextFieldValue(mapViewModel.getValueFromPref("vehicleNumber").toString())
        name.value = mapViewModel.getValueFromPref("name").toString()
        phoneNumber.value = mapViewModel.getValueFromPref("number").toString()
        firmName.value = mapViewModel.getValueFromPref("firmName").toString()

        if (appId.isNullOrBlank() || signature.isNullOrBlank()) {
            authRedirect()
        } else {
            getDeviceList(appId, signature)

            while (selectedScreen.value == 1) {
                delay(2000)
                getLiveLocation()
            }
        }
    }

    LaunchedEffect(selectedScreen.value) {
        if (toggle.value) {
            createPolyline()
        }
    }

    Scaffold(
        topBar = {
            TopBar(
                drawerState = drawerState,
                value = selectedVehicle.value.text,
                onValueChange = {
                    selectedVehicle.value = TextFieldValue(it)
                    onVehicleChange(it)
                },
                options = vehicleList.map { it.vehicleNumber },
                selectedScreen = selectedScreen
            )
        },

        bottomBar = {
            BottomBar(selectedScreen)
        },

        containerColor = Color.White,

        ) {
        Column(modifier = Modifier.padding(it)) {

            if (selectedScreen.value == 1) {
                isPlaybackStarted.value = false
            }

            if (selectedScreen.value == 2) {
                toggle.value = true
                PlaybackScreen(
                    playbackLatLngList = playbackLatLngList,
                    isPlaybackStarted = isPlaybackStarted,
                    activeSpeedButton = activeSpeedButton,
                    initiallySelectedVehicle = selectedVehicle.value.text,
                    vehicleList = vehicleList,
                    currentSpeed = currentSpeed
                )
            }

            MapViewer(
                lat = lat.value.toDouble(),
                lng = lng.value.toDouble(),
                playbackLatLngList = playbackLatLngList,
                isPlaybackStarted = isPlaybackStarted,
                activeSpeedButton = activeSpeedButton,
                carIcon = carIcon,
                currentSpeed = currentSpeed
            )
        }

    }

    if (showLoadingScreen) {
        Box(modifier = Modifier.fillMaxSize()) { LoadingScreen() }
    }

    if (drawerState.value) {
        NavigationDrawer(
            onCloseClick = { drawerState.value = false },
            name = name,
            phoneNumber = phoneNumber,
            firmName = firmName,
            onLogoutClick = { onLogoutClick() }
        )
    }
}


