package com.example.trackerapp.util

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.example.trackerapp.ui.theme.PrimaryGreen

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0x59FFFFFF)),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Loading...", color = PrimaryGreen, fontSize = 20.sp)
    }
}