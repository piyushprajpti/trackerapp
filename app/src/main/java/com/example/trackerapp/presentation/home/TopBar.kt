package com.example.trackerapp.presentation.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.trackerapp.ui.theme.PrimaryGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    drawerState: MutableState<Boolean>,
    value: String,
    onValueChange: (String) -> Unit,
    options: List<String>,
    selectedScreen: MutableState<Int>
) {

    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryGreen),
        navigationIcon = {
            IconButton(onClick = { drawerState.value = true }) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu Icon",
                    tint = Color.White
                )
            }
        },
        title = {
            when (selectedScreen.value) {
                1 -> {
                    VehicleDropDownList(
                        value = value,
                        onValueChange = onValueChange,
                        options = options,
                        isPlaybackScreen = false
                    )
                }

                2 -> {
                    Text(
                        text = "History Playback",
                        color = Color.White,
                        fontSize = 20.sp
                    )
                }
            }
        }
    )
}