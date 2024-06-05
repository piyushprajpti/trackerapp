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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    var lat = remember {
        mutableStateOf(27.887046)
    }
    var long = remember {
        mutableStateOf(76.287877)
    }


    LaunchedEffect(Unit) {
        launch {
            while (true) {
                val newLat = lat.value + 1 // Update with a small increment
                val newLong = long.value + 1
                lat.value = newLat
                long.value = newLong
                delay(1000)
            }
        }
    }

    var userID by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        viewModel.getUserIdFromPref {
            userID = it
        }
    }

    ModalNavigationDrawer(
        drawerContent = {
            NavigationDrawer(
                onCloseClick = { coroutineScope.launch { drawerState.close() } }
            )
        },
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
                                contentDescription = "Menu Icon",
                                tint = Color.White
                            )
                        }
                    }
                )
            },
            containerColor = Color.White,
        ) {
            Column(modifier = Modifier.padding(it)) {
                MapViewer(lat.value, long.value)
            }
        }
    }
}


@Composable
fun MapViewer(lat: Double, long: Double) {
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
                            )
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

//@Composable
//fun AppNavigation() {
//    val navController = rememberNavController()
//    NavHost(navController = navController, startDestination = "direction_fragment") {
//        composable("direction_fragment") {
//            val context = LocalContext.current
//            val directionFragment = remember { DirectionFragment.newInstance() }
//            directionFragment.show(context.supportFragmentManager, DirectionFragment::class.java.simpleName)
//        }
//        composable("place_autocomplete_fragment") {
//            val context = LocalContext.current
//            val placeAutocompleteFragment = remember { DirectionFragment.newInstance(directionOptions) }
//            placeAutocompleteFragment.show(context.supportFragmentManager, PlaceAutocompleteFragment::class.java.simpleName)
//        }
//    }
//}
