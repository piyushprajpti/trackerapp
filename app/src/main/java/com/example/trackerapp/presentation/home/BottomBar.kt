package com.example.trackerapp.presentation.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import com.example.trackerapp.ui.theme.PrimaryGreen

@Composable
fun BottomBar(selectedScreen: MutableState<Int>) {
    BottomAppBar(
        containerColor = PrimaryGreen
    ) {
        NavigationBar(
            containerColor = PrimaryGreen,
        ) {
            NavigationBarItem(
                selected = selectedScreen.value == 1,
                onClick = { selectedScreen.value = 1 },
                label = { Text(text = "Home") },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color(0x1AFFFFFF),
                    selectedIconColor = Color.White,
                    selectedTextColor = Color.White,
                    unselectedIconColor = Color.LightGray,
                    unselectedTextColor = Color.LightGray
                ),
                icon = {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "home"
                    )
                }
            )
            NavigationBarItem(
                selected = selectedScreen.value == 2,
                onClick = { selectedScreen.value = 2 },
                label = { Text(text = "History Playback") },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color(0x1AFFFFFF),
                    selectedIconColor = Color.White,
                    selectedTextColor = Color.White,
                    unselectedIconColor = Color.LightGray,
                    unselectedTextColor = Color.LightGray
                ),
                icon = {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = "history"
                    )
                }
            )
        }
    }
}