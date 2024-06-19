package com.example.trackerapp.presentation.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import com.example.trackerapp.ui.theme.PrimaryGreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    drawerState: MutableState<Boolean>,
    value: String,
    onValueChange: (String) -> Unit,
    options: List<String>
) {

    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryGreen),
        navigationIcon = {
            IconButton(onClick = {drawerState.value = true}) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu Icon",
                    tint = Color.White
                )
            }
        },
        title = {
            VehicleDropDownList(
                value = value,
                onValueChange = onValueChange,
                options = options
            )
        }
    )
}