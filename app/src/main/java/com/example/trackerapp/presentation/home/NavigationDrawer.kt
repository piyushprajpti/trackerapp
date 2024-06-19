package com.example.trackerapp.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trackerapp.ui.theme.PrimaryGreen

@Composable
fun NavigationDrawer(
    onCloseClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.75f)
                .fillMaxHeight()
                .background(Color.White)
                .padding(start = 15.dp, top = 15.dp)
        ) {
            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "Account Information",
                color = PrimaryGreen,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "Name - Piyush Prajapati",
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Number - 7014986896",
                fontSize = 16.sp
            )

        }

        Box(modifier = Modifier
            .fillMaxSize()
            .clickable { onCloseClick() })

    }
}