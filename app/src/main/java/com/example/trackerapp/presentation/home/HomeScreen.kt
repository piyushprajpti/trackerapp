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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.trackerapp.R
import com.example.trackerapp.domain.model.mapModels.deviceList.VehicleList
import com.example.trackerapp.domain.model.mapModels.historyPlayback.PlaybackLatLngList
import com.example.trackerapp.presentation.map.MapViewModel
import com.example.trackerapp.presentation.map.MapViewer
import com.example.trackerapp.ui.theme.PrimaryOrange
import com.example.trackerapp.util.LoadingScreen
import com.example.trackerapp.util.Response
import com.mappls.sdk.maps.MapView
import com.mappls.sdk.maps.MapplsMap
import com.mappls.sdk.maps.OnMapReadyCallback
import com.mappls.sdk.maps.annotations.Icon
import com.mappls.sdk.maps.annotations.IconFactory
import com.mappls.sdk.maps.annotations.MarkerOptions
import com.mappls.sdk.maps.annotations.PolylineOptions
import com.mappls.sdk.maps.camera.CameraPosition
import com.mappls.sdk.maps.geometry.LatLng
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
        IconFactory.getInstance(context).fromResource(R.drawable.car)
    }

    var vehicleList by remember {
        mutableStateOf<List<VehicleList>>(emptyList())
    }

    val selectedVehicle = remember {
        mutableStateOf(TextFieldValue(""))
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

    fun createPolyline() {

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        val endTime = calendar.timeInMillis / 1000

        calendar.add(Calendar.DAY_OF_YEAR, -1)
        val startTime = calendar.timeInMillis / 1000

        mapViewModel.getHistoryPlayback(
            imei = vehicleList.find { selectedVehicle.value.text == it.vehicleNumber }?.imei
                ?: "",
            startTime = startTime,
            endTime = endTime,
            callBack = {
                when (it) {
                    is Response.Success -> {
                        if (it.data.playbackLatLngList.isEmpty()) {
                            Toast.makeText(
                                context,
                                "No vehicle movement records found in last 24 hours",
                                Toast.LENGTH_LONG
                            ).show()
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

    var phoneNumber = remember {
        mutableStateOf("")
    }

    val firmName = remember {
        mutableStateOf("")
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

//        Log.d("TAG", "HomeScreen: $signature")
//        Log.d("TAG", "HomeScreen: $imei")

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
                PlaybackScreen(
                    playbackLatLngList = playbackLatLngList,
                    isPlaybackStarted = isPlaybackStarted,
                    activeSpeedButton = activeSpeedButton,
                    initiallySelectedVehicle = selectedVehicle.value.text,
                    vehicleList = vehicleList
                )
            }

            MapViewer(
                lat = lat.value.toDouble(),
                lng = lng.value.toDouble(),
                playbackLatLngList = playbackLatLngList,
                isPlaybackStarted = isPlaybackStarted,
                activeSpeedButton = activeSpeedButton,
                carIcon = carIcon
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


