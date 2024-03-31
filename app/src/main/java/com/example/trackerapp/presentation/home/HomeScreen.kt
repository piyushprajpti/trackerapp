package com.example.trackerapp.presentation.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.trackerapp.ui.theme.PrimaryGreen
import com.mappls.sdk.maps.MapView
import com.mappls.sdk.maps.MapplsMap
import com.mappls.sdk.maps.OnMapReadyCallback
import com.mappls.sdk.maps.annotations.MarkerOptions
import com.mappls.sdk.maps.camera.CameraPosition
import com.mappls.sdk.maps.geometry.LatLng
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    val lat = remember {
        29.1492
    }

    val long = remember {
        75.7217
    }

    ModalNavigationDrawer(
        drawerContent = { NavigationDrawer() },
        drawerState = drawerState,
        gesturesEnabled = false,
        modifier = Modifier.background(Color.White)
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryGreen),
                    title = { Text(text = "Tracker App", color = Color.White) },
                    navigationIcon = {
                        IconButton(onClick = {
                            coroutineScope.launch {
                                drawerState.open()
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu Icon"
                            )
                        }
                    }
                )
            },
            containerColor = Color.White,
        ) {
            Column(modifier = Modifier.padding(it)) {
                CustomView(lat, long)
            }
        }
    }
}


@Composable
fun CustomView(lat: Double, long: Double) {
    AndroidView(
        modifier = Modifier.fillMaxSize(), // Occupy the max size in the Compose UI tree
        factory = { context ->
            // Creates view
            MapView(context).apply {
                getMapAsync(object : OnMapReadyCallback {
                    override fun onMapReady(mapplsMap: MapplsMap) {
                        mapplsMap.addMarker(
                            MarkerOptions().position(
                                LatLng(lat, long)
                            ).title("Satish's Test Location")
                        )

                        /* this is done for animating/moving camera to particular position */
                        val cameraPosition = CameraPosition.Builder().target(
                            LatLng(lat, long)
                        ).zoom(10.0).tilt(0.0).build()
                        mapplsMap.cameraPosition = cameraPosition
                    }

                    override fun onMapError(p0: Int, p1: String?) {
                        Log.d("satish", p1.toString())
                    }
                })
            }
        },
        update = { view ->

        }
    )
}
