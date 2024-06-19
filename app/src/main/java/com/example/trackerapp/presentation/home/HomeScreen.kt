package com.example.trackerapp.presentation.home

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.trackerapp.domain.model.mapModels.deviceList.VehicleList
import com.example.trackerapp.domain.model.mapModels.historyPlayback.Info
import com.example.trackerapp.presentation.map.MapViewModel
import com.example.trackerapp.ui.theme.PrimaryGreen
import com.example.trackerapp.util.LoadingScreen
import com.example.trackerapp.util.Response
import com.mappls.sdk.maps.MapView
import com.mappls.sdk.maps.MapplsMap
import com.mappls.sdk.maps.OnMapReadyCallback
import com.mappls.sdk.maps.annotations.MarkerOptions
import com.mappls.sdk.maps.camera.CameraPosition
import com.mappls.sdk.maps.geometry.LatLng
import kotlinx.coroutines.delay

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    mapViewModel: MapViewModel = hiltViewModel(),
    authRedirect: () -> Unit
) {
    var drawerState = remember {
        mutableStateOf(false)
    }

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

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
        mutableStateOf("27.887046")
    }
    val lng = remember {
        mutableStateOf("76.287877")
    }

    var playbackLatLng by remember {
        mutableStateOf<List<Info>>(emptyList())
    }

    val showPlaybackBox = remember {
        mutableStateOf(false)
    }

    fun getDeviceList(appid: String?, signature: String?, imei: String?) {
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
                                    selectedVehicle.value = TextFieldValue(
                                        vehicleList.find { it.imei == imei.toString() }?.vehicleNumber
                                            ?: ""
                                    )
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
                        Log.d("TAG", "getDeviceList: $it")
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
                }

                is Response.Error -> {

                }
            }
        }
    }

    LaunchedEffect(Unit) {

        val appid = mapViewModel.getValueFromPref("appid")
        val signature = mapViewModel.getValueFromPref("signature")
        val imei = mapViewModel.getValueFromPref("imei")

        Log.d("TAG", "HomeScreen: $appid")
        Log.d("TAG", "HomeScreen: $signature")
        Log.d("TAG", "HomeScreen: $imei")

        if (appid.isNullOrBlank() || signature.isNullOrBlank() || imei.isNullOrBlank()) {
            authRedirect()
        } else {
            getDeviceList(appid, signature, imei)

            while (!showPlaybackBox.value) {
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
                onValueChange = { selectedVehicle.value = TextFieldValue(it) },
                options = vehicleList.map { it.vehicleNumber }
            )
        },

        bottomBar = {
            BottomBar()
        },

        containerColor = Color.White,

        floatingActionButton = {
            if (!showPlaybackBox.value) {
                IconButton(
                    onClick = {
                        showPlaybackBox.value = true

                    },
                    modifier = Modifier
                        .padding(10.dp)
                        .background(PrimaryGreen, CircleShape)
                        .padding(5.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = "",
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        },

        floatingActionButtonPosition = FabPosition.End
    ) {
        Column(modifier = Modifier.padding(it)) {
            MapViewer(lat.value.toDouble(), lng.value.toDouble(), playbackLatLng)
        }

        if (showPlaybackBox.value) PlaybackBox(showPlaybackBox = showPlaybackBox)
    }

    if (showLoadingScreen) {
        Box(modifier = Modifier.fillMaxSize()) { LoadingScreen() }
    }

    if (drawerState.value) {
        NavigationDrawer(onCloseClick = { drawerState.value = false })
    }
}

@Composable
fun MapViewer(lat: Double, lng: Double, playbackLatLng: List<Info>) {
    val mapplsMapState = remember { mutableStateOf<MapplsMap?>(null) }
    var currentIndex by remember { mutableStateOf(0) }

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            MapView(context).apply {
                getMapAsync(object : OnMapReadyCallback {
                    override fun onMapReady(mapplsMap: MapplsMap) {
                        mapplsMapState.value = mapplsMap
                        val initialPosition = LatLng(lat, lng)
                        mapplsMap.addMarker(MarkerOptions().position(initialPosition))

                        val cameraPosition =
                            CameraPosition.Builder().target(initialPosition).zoom(15.0).tilt(0.0)
                                .build()
                        mapplsMap.cameraPosition = cameraPosition
                    }

                    override fun onMapError(p0: Int, p1: String?) {
                        Toast.makeText(
                            context,
                            "Something went wrong. Please restart the app",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
            }
        },
        update = {
            mapplsMapState.value?.let { mapplsMap ->
                mapplsMap.clear()
                mapplsMap.addMarker(MarkerOptions().position(LatLng(lat, lng)))
                mapplsMap.cameraPosition =
                    CameraPosition.Builder().target(LatLng(lat, lng)).zoom(15.0).tilt(0.0).build()
            }

        }
    )
}