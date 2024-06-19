package com.example.trackerapp.util

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

class DateUtils {
    @RequiresApi(Build.VERSION_CODES.O)
    fun convertMillisToLocalDate(millis: Long) : LocalDate {
        return Instant
            .ofEpochMilli(millis)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun convertMillisToLocalDateWithFormatter(date: LocalDate, dateTimeFormatter: DateTimeFormatter) : LocalDate {
        //Convert the date to a long in millis using a dateformmater
        val dateInMillis = LocalDate.parse(date.format(dateTimeFormatter), dateTimeFormatter)
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

        //Convert the millis to a localDate object
        return Instant
            .ofEpochMilli(dateInMillis)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun dateToString(date: LocalDate): String {
        val dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.getDefault())
        val dateInMillis = convertMillisToLocalDateWithFormatter(date, dateFormatter)
        return dateFormatter.format(dateInMillis)
    }
}


@Composable
fun TimePickerDialog(
    title: String = "Select Time",
    onDismissRequest: () -> Unit,
    confirmButton: @Composable (() -> Unit),
    dismissButton: @Composable (() -> Unit)? = null,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp,
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .height(IntrinsicSize.Min)
                .background(
                    shape = MaterialTheme.shapes.extraLarge,
                    color = containerColor
                ),
            color = containerColor
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    text = title,
                    style = MaterialTheme.typography.labelMedium
                )
                content()
                Row(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    dismissButton?.invoke()
                    confirmButton()
                }
            }
        }
    }
}


fun convertTo12HourFormat(hour: Int): Pair<Int, String> {
    val period = if (hour < 12) "AM" else "PM"
    val adjustedHour = when {
        hour == 0 -> 12
        hour > 12 -> hour - 12
        else -> hour
    }
    return Pair(adjustedHour, period)
}