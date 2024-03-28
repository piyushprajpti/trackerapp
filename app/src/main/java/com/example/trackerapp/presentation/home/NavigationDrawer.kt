package com.example.trackerapp.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun NavigationDrawer() {
    Column(
        modifier = Modifier.fillMaxHeight().fillMaxWidth(.75f).background(Color.White)
    ) {
        Box {
            Text(text = "Navigation Drawer")
        }
    }
}