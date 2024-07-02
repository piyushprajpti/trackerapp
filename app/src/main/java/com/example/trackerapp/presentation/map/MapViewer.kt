package com.example.trackerapp.presentation.map

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
import com.example.trackerapp.domain.model.mapModels.historyPlayback.PlaybackLatLngList
import com.example.trackerapp.ui.theme.PrimaryOrange
import com.mappls.sdk.maps.MapView
import com.mappls.sdk.maps.MapplsMap
import com.mappls.sdk.maps.OnMapReadyCallback
import com.mappls.sdk.maps.annotations.Icon
import com.mappls.sdk.maps.annotations.MarkerOptions
import com.mappls.sdk.maps.annotations.PolylineOptions
import com.mappls.sdk.maps.camera.CameraPosition
import com.mappls.sdk.maps.geometry.LatLng
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MapViewer(
    lat: Double,
    lng: Double,
    playbackLatLngList: MutableState<List<PlaybackLatLngList>>,
    isPlaybackStarted: MutableState<Boolean>,
    activeSpeedButton: MutableState<Int>,
    carIcon: Icon
) {
    val mapplsMapState = remember { mutableStateOf<MapplsMap?>(null) }
    val coroutineScope = rememberCoroutineScope()

    val dataFetched = remember {
        mutableStateOf(false)
    }

    LaunchedEffect(playbackLatLngList.value.isNotEmpty()) {
        dataFetched.value = true
    }

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
                if (isPlaybackStarted.value) {
                    coroutineScope.launch {
                        animatePolyline(
                            mapplsMap = mapplsMap,
                            playbackLatLngList = playbackLatLngList.value,
                            activeSpeedButton = activeSpeedButton,
                            isPlaybackStarted = isPlaybackStarted,
                            carIcon = carIcon
                        )
                    }
                } else if (dataFetched.value) {
                    mapplsMap.clear()
                    mapplsMap.addMarker(MarkerOptions().position(LatLng(lat, lng)))
                    mapplsMap.cameraPosition =
                        CameraPosition.Builder().target(LatLng(lat, lng)).zoom(15.0).tilt(0.0)
                            .build()
                    mapplsMap.addPolyline(PolylineOptions().width(3f).color(PrimaryOrange.toArgb()).addAll(playbackLatLngList.value.map {
                        LatLng(
                            it.lat.toDouble(),
                            it.lng.toDouble()
                        )
                    }))
                }
            }
        }
    )
}

suspend fun animatePolyline(
    mapplsMap: MapplsMap,
    playbackLatLngList: List<PlaybackLatLngList>,
    activeSpeedButton: MutableState<Int>,
    isPlaybackStarted: MutableState<Boolean>,
    carIcon: Icon
) {
    mapplsMap.clear()
    if (playbackLatLngList.isNotEmpty()) {
        val polylineOptions = PolylineOptions().width(3f).color(PrimaryOrange.toArgb())

        // Create a marker at the starting position
        val marker = mapplsMap.addMarker(
            MarkerOptions().position(
                LatLng(
                    playbackLatLngList[0].lat.toDouble(),
                    playbackLatLngList[0].lng.toDouble()
                )
            )
        )

        // Animate the marker along the polyline
        for (i in playbackLatLngList.indices) {
            if (!isPlaybackStarted.value) {
                mapplsMap.clear()
                break
            }
            val latLng = LatLng(
                playbackLatLngList[i].lat.toDouble(),
                playbackLatLngList[i].lng.toDouble()
            )

            // Update the polyline with the new point
            polylineOptions.add(latLng)
            mapplsMap.addPolyline(polylineOptions)

            // Update the marker position
            marker.position = latLng
            mapplsMap.cameraPosition =
                CameraPosition.Builder().target(latLng).build()

            val delayValue = when (activeSpeedButton.value) {
                10 -> 5L
                5 -> 100L
                2 -> 250L
                else -> 500L
            }

            delay(delayValue)
        }
    }
}