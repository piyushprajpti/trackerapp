package com.example.trackerapp.presentation.home

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.trackerapp.ui.theme.PrimaryGreen

@Composable
fun BottomBar(modifier: Modifier = Modifier) {
    BottomAppBar(
        containerColor = PrimaryGreen
    ) {
        NavigationBar(
            containerColor = PrimaryGreen,
        ) {
            NavigationBarItem(
                selected = true,
                onClick = { /*TODO*/ },
                label = { Text(text = "Home")},
                icon = {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "home"
                    )
                }
            )
            NavigationBarItem(
                selected = false,
                onClick = { /*TODO*/ },
                label = { Text(text = "History Playback")},
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