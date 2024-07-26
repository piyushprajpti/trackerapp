package com.example.trackerapp.presentation.map

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalContext
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.example.trackerapp.R
import com.example.trackerapp.domain.model.mapModels.historyPlayback.PlaybackLatLngList
import com.example.trackerapp.ui.theme.PrimaryOrange
import com.mappls.sdk.direction.ui.DirectionFragment
import com.mappls.sdk.direction.ui.model.DirectionOptions
import com.mappls.sdk.maps.MapView
import com.mappls.sdk.maps.MapplsMap
import com.mappls.sdk.maps.OnMapReadyCallback
import com.mappls.sdk.maps.annotations.Icon
import com.mappls.sdk.maps.annotations.IconFactory
import com.mappls.sdk.maps.annotations.MarkerOptions
import com.mappls.sdk.maps.annotations.PolylineOptions
import com.mappls.sdk.maps.camera.CameraPosition
import com.mappls.sdk.maps.geometry.LatLng
import com.mappls.sdk.services.api.directions.DirectionsCriteria
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MapViewer(
    lat: Double,
    lng: Double,
    playbackLatLngList: MutableState<List<PlaybackLatLngList>>,
    isPlaybackStarted: MutableState<Boolean>,
    activeSpeedButton: MutableState<Int>,
    carIcon: Icon,
    currentSpeed: MutableState<Int>
) {
    val mapplsMapState = remember { mutableStateOf<MapplsMap?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val localContext = LocalContext.current

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
                            carIcon = carIcon,
                            currentSpeed = currentSpeed,
                            localContext = localContext
                        )
                    }
                } else if (dataFetched.value) {
                    mapplsMap.clear()
                    if (playbackLatLngList.value.isNotEmpty()) {
                        var previousLatLng = LatLng(
                            playbackLatLngList.value[0].lat.toDouble(),
                            playbackLatLngList.value[0].lng.toDouble()
                        )
                        val polylineOptions = PolylineOptions().width(3f).color(PrimaryOrange.toArgb())
                        polylineOptions.add(previousLatLng)

                        // Create a marker at the starting position with the initial direction
                        var bearing = calculateBearing(
                            previousLatLng,
                            LatLng(
                                playbackLatLngList.value[1].lat.toDouble(),
                                playbackLatLngList.value[1].lng.toDouble()
                            )
                        )
                        var rotatedCarIconBitmap = carIcon.bitmap.rotate(bearing)
                        var rotatedCarIcon = IconFactory.getInstance(it.context).fromBitmap(rotatedCarIconBitmap)
                        var marker = mapplsMap.addMarker(
                            MarkerOptions().position(previousLatLng).icon(rotatedCarIcon)
                        )

                        // Add polyline and markers for all points
                        for (i in 1 until playbackLatLngList.value.size) {
                            val currentLatLng = LatLng(
                                playbackLatLngList.value[i].lat.toDouble(),
                                playbackLatLngList.value[i].lng.toDouble()
                            )

                            // Calculate the bearing between the previous and current point
                            bearing = calculateBearing(previousLatLng, currentLatLng)

                            // Rotate the car icon
                            rotatedCarIconBitmap = carIcon.bitmap.rotate(bearing)
                            rotatedCarIcon = IconFactory.getInstance(it.context).fromBitmap(rotatedCarIconBitmap)

                            // Update the polyline with the new point
                            polylineOptions.add(currentLatLng)

                            // Remove the old marker and add a new one with the rotated icon
                            marker.remove()
                            marker = mapplsMap.addMarker(
                                MarkerOptions().position(currentLatLng).icon(rotatedCarIcon)
                            )

                            previousLatLng = currentLatLng
                        }

                        mapplsMap.addPolyline(polylineOptions)
                        mapplsMap.cameraPosition =
                            CameraPosition.Builder().target(LatLng(lat, lng)).zoom(15.0).tilt(0.0)
                                .build()
                    }
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
    carIcon: Icon,
    currentSpeed: MutableState<Int>,
    localContext: Context
) {
    mapplsMap.clear()
    if (playbackLatLngList.isNotEmpty()) {
        val polylineOptions = PolylineOptions().width(3f).color(PrimaryOrange.toArgb())

        // Create a marker at the starting position
        var marker = mapplsMap.addMarker(
            MarkerOptions().position(
                LatLng(
                    playbackLatLngList[0].lat.toDouble(),
                    playbackLatLngList[0].lng.toDouble()
                )
            ).icon(carIcon)
        )

        // Animate the marker along the polyline
        for (i in 1 until playbackLatLngList.size) {
            if (!isPlaybackStarted.value) {
                mapplsMap.clear()
                break
            }
            val previousLatLng = LatLng(
                playbackLatLngList[i - 1].lat.toDouble(),
                playbackLatLngList[i - 1].lng.toDouble()
            )
            val currentLatLng = LatLng(
                playbackLatLngList[i].lat.toDouble(),
                playbackLatLngList[i].lng.toDouble()
            )
            currentSpeed.value = playbackLatLngList[i].speed

            // Calculate the bearing between the previous and current point
            val bearing = calculateBearing(previousLatLng, currentLatLng)

            // Rotate the car icon
            val rotatedCarIconBitmap = carIcon.bitmap.rotate(bearing)
            val rotatedCarIcon = IconFactory.getInstance(localContext).fromBitmap(rotatedCarIconBitmap)

            // Update the polyline with the new point
            polylineOptions.add(currentLatLng)
            mapplsMap.addPolyline(polylineOptions)

            // Remove the old marker and add a new one with the rotated icon
            marker.remove()
            marker = mapplsMap.addMarker(
                MarkerOptions().position(currentLatLng).icon(rotatedCarIcon)
            )

            mapplsMap.cameraPosition =
                CameraPosition.Builder().target(currentLatLng).build()

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

fun Bitmap.rotate(degrees: Float): Bitmap {
    val matrix = Matrix().apply { postRotate(degrees) }
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}

fun calculateBearing(start: LatLng, end: LatLng): Float {
    val lat1 = Math.toRadians(start.latitude)
    val lon1 = Math.toRadians(start.longitude)
    val lat2 = Math.toRadians(end.latitude)
    val lon2 = Math.toRadians(end.longitude)

    val dLon = lon2 - lon1
    val y = Math.sin(dLon) * Math.cos(lat2)
    val x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1) * Math.cos(lat2) * Math.cos(dLon)
    val bearing = Math.toDegrees(Math.atan2(y, x))

    return ((bearing + 360) % 360).toFloat()
}