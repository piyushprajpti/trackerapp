package com.example.trackerapp.presentation.home

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.trackerapp.domain.model.mapModels.deviceList.VehicleList
import com.example.trackerapp.domain.model.mapModels.historyPlayback.PlaybackLatLngList
import com.example.trackerapp.presentation.map.MapViewModel
import com.example.trackerapp.ui.theme.PrimaryGreen
import com.example.trackerapp.util.DateUtils
import com.example.trackerapp.util.LoadingScreen
import com.example.trackerapp.util.Response
import com.example.trackerapp.util.TimePickerDialog
import com.example.trackerapp.util.convertTo12HourFormat

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaybackScreen(
    playbackLatLngList: MutableState<List<PlaybackLatLngList>>,
    isPlaybackStarted: MutableState<Boolean>,
    activeSpeedButton: MutableState<Int>,
    initiallySelectedVehicle: String,
    vehicleList: List<VehicleList>,
    currentSpeed: MutableState<Int>,
    mapViewModel: MapViewModel = hiltViewModel()
) {

    val isStartTime = remember {
        mutableStateOf(true)
    }

    val startDateState =
        rememberDatePickerState(initialSelectedDateMillis = System.currentTimeMillis())
    val endDateState =
        rememberDatePickerState(initialSelectedDateMillis = System.currentTimeMillis())

    val startMillisToLocalDate = startDateState.selectedDateMillis?.let {
        DateUtils().convertMillisToLocalDate(it)
    }

    val endMillisToLocalDate = endDateState.selectedDateMillis?.let {
        DateUtils().convertMillisToLocalDate(it)
    }

    val startDate = startMillisToLocalDate?.let {
        DateUtils().dateToString(startMillisToLocalDate)
    } ?: ""

    val endDate = endMillisToLocalDate?.let {
        DateUtils().dateToString(endMillisToLocalDate)
    } ?: ""

    val showDatePickerBox = remember {
        mutableStateOf(false)
    }

    if (showDatePickerBox.value) {
        DatePickerDialog(
            onDismissRequest = {
                showDatePickerBox.value = false
            },
            confirmButton = {
                TextButton(onClick = { showDatePickerBox.value = false }) {
                    Text(text = "OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePickerBox.value = false }) {
                    Text(text = "CANCEL")
                }
            },
            content = {
                if (isStartTime.value) {
                    DatePicker(state = startDateState)
                } else {
                    DatePicker(state = endDateState)
                }
            }
        )
    }


    val startTimeState =
        rememberTimePickerState(initialHour = 12, initialMinute = 12)
    val endTimeState =
        rememberTimePickerState(initialHour = 12, initialMinute = 12)

    val showTimePickerBox = remember {
        mutableStateOf(false)
    }

    val (startHour, startAMorPM) = convertTo12HourFormat(startTimeState.hour)
    val (endHour, endAMorPM) = convertTo12HourFormat(endTimeState.hour)

    val startMinute = remember {
        mutableIntStateOf(startTimeState.minute)
    }

    val endMinute = remember {
        mutableIntStateOf(endTimeState.minute)
    }

    if (showTimePickerBox.value) {
        TimePickerDialog(
            onDismissRequest = {
                showTimePickerBox.value = false
            },
            confirmButton = {
                TextButton(onClick = { showTimePickerBox.value = false }) {
                    Text(text = "OK")
                }
                if (isStartTime.value) {
                    startMinute.intValue = startTimeState.minute
                } else {
                    endMinute.intValue = endTimeState.minute
                }
            },
            dismissButton = {
                TextButton(onClick = { showTimePickerBox.value = false }) {
                    Text(text = "CANCEL")
                }
            },
            content = {
                if (isStartTime.value) {
                    TimePicker(state = startTimeState)
                } else {
                    TimePicker(state = endTimeState)
                }
            }
        )
    }

    val selectedVehicle = remember {
        mutableStateOf(TextFieldValue(initiallySelectedVehicle))
    }

    val isLoading = remember {
        mutableStateOf(false)
    }

    val maxSpeed = remember {
        mutableStateOf(0)
    }

    if (maxSpeed.value < currentSpeed.value) maxSpeed.value = currentSpeed.value

    val context = LocalContext.current

    fun onPlayClick() {
        val startDateMillis = startDateState.selectedDateMillis ?: System.currentTimeMillis()
        val startMillis =
            (startDateMillis / 1000) + (startTimeState.hour * 3600) + (startTimeState.minute * 3600)

        val endDateMillis = endDateState.selectedDateMillis ?: System.currentTimeMillis()
        val endMillis =
            (endDateMillis / 1000) + (endTimeState.hour * 3600) + (endTimeState.minute * 3600)

        if (startMillis > endMillis) {
            Toast.makeText(context, "Start time can't be greater than end time", Toast.LENGTH_LONG)
                .show()
        } else if (startMillis == endMillis) {
            Toast.makeText(context, "Start time can't be equal to end time", Toast.LENGTH_LONG)
                .show()
        } else {
            mapViewModel.getHistoryPlayback(
                imei = vehicleList.find { selectedVehicle.value.text == it.vehicleNumber }?.imei
                    ?: "",
                startTime = startMillis,
                endTime = endMillis,
                callBack = {
                    when (it) {
                        is Response.Success -> {
                            if (it.data.result == "时间跨度不能超过1个月") {
                                Toast.makeText(
                                    context,
                                    "Time range can't exceed one month",
                                    Toast.LENGTH_LONG
                                ).show()
                            } else if (it.data.result == "仅能获取9个月内的数据") {
                                Toast.makeText(
                                    context,
                                    "Data for the last 9 months is only available",
                                    Toast.LENGTH_LONG
                                ).show()
                            } else if (it.data.playbackLatLngList.isEmpty()) {
                                Toast.makeText(
                                    context,
                                    "No vehicle movement records found in selected time range",
                                    Toast.LENGTH_LONG
                                ).show()
                            } else {
                                isPlaybackStarted.value = true
                                playbackLatLngList.value = it.data.playbackLatLngList.map {
                                    PlaybackLatLngList(
                                        code = it.code,
                                        imei = it.imei,
                                        lat = it.lat,
                                        lng = it.lng,
                                        speed = it.speed,
                                        course = it.course,
                                        gpsTime = it.gpsTime,
                                        positionType = it.positionType
                                    )
                                }
                            }
                        }

                        is Response.Error -> {
                            Log.d("TAG", "onPlayClick: ${it.error}")
                            Toast.makeText(
                                context,
                                "Unable to start playback. Please try again",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            )
        }
    }

    val screenSizeModifier =
        if (isPlaybackStarted.value) Modifier.fillMaxWidth() else Modifier.fillMaxSize()

    Column(
        modifier = Modifier
            .background(Color.Transparent)
            .then(screenSizeModifier)
            .padding(start = 10.dp, top = 10.dp, end = 10.dp, bottom = 5.dp)
    ) {

        if (isLoading.value) {
            Box(modifier = Modifier.fillMaxSize()) { LoadingScreen() }
        }
//            heading
        if (!isPlaybackStarted.value) {

            Text(
                text = "Select Vehicle",
                color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(10.dp))

            VehicleDropDownList(
                value = selectedVehicle.value.text,
                onValueChange = { selectedVehicle.value = TextFieldValue(it) },
                options = vehicleList.map { it.vehicleNumber },
                isPlaybackScreen = true
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
//                1. start time
                Column(modifier = Modifier.weight(0.5f)) {
                    Text(
                        text = "Start Time",
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    HorizontalDivider(
                        color = Color(0xFFF0F0F0),
                        modifier = Modifier.fillMaxWidth(.9f)
                    )

//                    start date
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = startDate, color = Color.Gray)

                        IconButton(onClick = {
                            isStartTime.value = true
                            showDatePickerBox.value = true
                        }) {
                            Icon(
                                imageVector = Icons.Default.CalendarMonth,
                                contentDescription = "",
                                tint = PrimaryGreen
                            )
                        }
                    }

//                    start time
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${startHour}:${startMinute.intValue} $startAMorPM",
                            color = Color.Gray
                        )

                        IconButton(onClick = {
                            isStartTime.value = true
                            showTimePickerBox.value = true
                        }) {
                            Icon(
                                imageVector = Icons.Default.AccessTime,
                                contentDescription = "",
                                tint = PrimaryGreen
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

//                2. end time
                Column(modifier = Modifier.weight(0.5f)) {
                    Text(
                        text = "End Time",
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    HorizontalDivider(
                        color = Color(0xFFF0F0F0),
                        modifier = Modifier.fillMaxWidth(.9f)
                    )

//                    end date
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = endDate, color = Color.Gray)

                        IconButton(onClick = {
                            isStartTime.value = false
                            showDatePickerBox.value = true
                        }) {
                            Icon(
                                imageVector = Icons.Default.CalendarMonth,
                                contentDescription = "",
                                tint = PrimaryGreen
                            )
                        }
                    }

//                    end time
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${endHour}:${endMinute.intValue} $endAMorPM",
                            color = Color.Gray
                        )

                        IconButton(onClick = {
                            isStartTime.value = false
                            showTimePickerBox.value = true
                        }) {
                            Icon(
                                imageVector = Icons.Default.AccessTime,
                                contentDescription = "",
                                tint = PrimaryGreen
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            TextButton(
                onClick = { onPlayClick() },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PrimaryGreen, RoundedCornerShape(8.dp))
            ) {
                Text(text = "PLAY", fontSize = 18.sp, color = Color.White)
            }

            Spacer(modifier = Modifier.height(10.dp))
        } else {

            // after play click
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row {
                        SpeedButton(
                            text = "1X",
                            isActive = activeSpeedButton.value == 1,
                            onClick = {
                                activeSpeedButton.value = 1
                            }
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        SpeedButton(
                            text = "2X",
                            isActive = activeSpeedButton.value == 2,
                            onClick = {
                                activeSpeedButton.value = 2
                            }
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        SpeedButton(
                            text = "5X",
                            isActive = activeSpeedButton.value == 5,
                            onClick = {
                                activeSpeedButton.value = 5
                            }
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        SpeedButton(
                            text = "10X",
                            isActive = activeSpeedButton.value == 10,
                            onClick = {
                                activeSpeedButton.value = 10
                            }
                        )
                    }

                    TextButton(
                        onClick = {
                            isPlaybackStarted.value = false
                        },
                        modifier = Modifier.background(Color.Red, RoundedCornerShape(14.dp))
                    ) {
                        Text(text = "STOP", color = Color.White, fontSize = 20.sp)
                    }
                }
                HorizontalDivider(
                    color = Color(0xFFECECEC),
                    modifier = Modifier.padding(top = 10.dp, bottom = 2.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Row(

                    ) {
                        Text(
                            text = "Current Speed: ",
                            color = Color.DarkGray,
                            fontSize = 13.sp
                        )
                        Text(
                            text = currentSpeed.value.toString(),
                            color = PrimaryGreen,
                            fontSize = 16.sp
                        )
                        Text(text = " Km/Hr", color = Color.DarkGray, fontSize = 13.sp)
                    }
                    VerticalDivider(modifier = Modifier.height(15.dp), color = Color(0xFFB8B8B8))
                    Row(

                    ) {
                        Text(
                            text = "Max Speed: ",
                            color = Color.DarkGray,
                            fontSize = 13.sp
                        )
                        Text(
                            text = maxSpeed.value.toString(),
                            color = PrimaryGreen,
                            fontSize = 16.sp
                        )
                        Text(text = " Km/Hr", color = Color.DarkGray, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun SpeedButton(text: String, isActive: Boolean, onClick: () -> Unit) {

    val backColor = if (isActive) Color(0xFFE1E1E1)
    else Color(0xFFF8F8F8)

    Box(
        modifier = Modifier
            .background(backColor, CircleShape)
            .padding(15.dp, 13.dp)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, color = Color.DarkGray, fontSize = 20.sp)
    }
}