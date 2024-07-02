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
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.trackerapp.ui.theme.PrimaryGreen

@Composable
fun NavigationDrawer(
    homeViewModel: HomeViewModel = hiltViewModel(),
    onCloseClick: () -> Unit,
    name: MutableState<String>,
    phoneNumber: MutableState<String>,
    firmName: MutableState<String>,
    onLogoutClick: () -> Unit
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

            InfoBox(
                title = "Name",
                description = name.value
            )

            Spacer(modifier = Modifier.height(10.dp))

            InfoBox(
                title = "Number",
                description = phoneNumber.value
            )

            Spacer(modifier = Modifier.height(10.dp))

            InfoBox(
                title = "Firm Name",
                description = firmName.value
            )

            Spacer(modifier = Modifier.height(70.dp))


            Text(
                text = "Log Out →",
                color = Color.Red,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable {onLogoutClick()}
            )
        }


        Box(modifier = Modifier
            .fillMaxSize()
            .clickable { onCloseClick() }
        )
    }
}

@Composable
fun InfoBox(
    title: String,
    description: String
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "$title  -  ",
            fontSize = 16.sp,
            color = PrimaryGreen,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = description,
            fontSize = 16.sp,
            color = Color.DarkGray
        )
    }
}