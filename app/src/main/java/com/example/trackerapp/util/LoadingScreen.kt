package com.example.trackerapp.util

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.trackerapp.ui.theme.PrimaryGreen

@Composable
fun LoadingScreen() {

    val infiniteTransition = rememberInfiniteTransition(label = "")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Restart
        ),
        label = ""
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
        .background(Color(0x19000000)),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.padding(20.dp)
//                .background(Color(0x0C000000), RoundedCornerShape(12.dp))
//                .border(1.dp, Color(0x20000000),RoundedCornerShape(12.dp))
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .rotate(rotation)
                    .padding(35.dp)
                    .size(40.dp),
                strokeWidth = 3.dp,
                color = Color.White
            )
        }
    }
}