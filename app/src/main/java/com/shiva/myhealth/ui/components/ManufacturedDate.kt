package com.shiva.myhealth.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shiva.myhealth.R
import com.shiva.myhealth.Screens.Screen
import com.shiva.myhealth.data.Day
import com.shiva.myhealth.ui.theme.MyHealthTheme
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManufacturedDate() {
    val dateState = rememberDatePickerState()
    val millisToLocalDate = dateState.selectedDateMillis?.let {
        DateUtils().convertMillisToLocalDate(it)
    }
    val dateToString = millisToLocalDate?.let {
        DateUtils().dateToString(millisToLocalDate)
    } ?: ""
    Column {
        DatePicker(
            /*dateFormatter = DatePickerFormatter(
                selectedDateSkeleton = "EE, dd MMM, yyyy",
            ),*/
            title = {
                Text(text = "Manufactured Date")
            },
            state = dateState,
            showModeToggle = true
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            text = dateToString
        )
    }
}


@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerWithDialog(
    modifier: Modifier = Modifier,
    selectedDay: MutableStateFlow<Day>,
    onSelectedDay: (Int) -> Unit

) {

    val dateState = rememberDatePickerState(
        selectableDates = object : SelectableDates {
            @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val currDay = Instant.ofEpochMilli(utcTimeMillis).atZone(ZoneId.of("UTC+3"))
                        .toLocalDate()
                    currDay.month >= LocalDate.now()
                        .minusMonths(1).month && currDay <= LocalDate.now() && currDay.year == LocalDate.now().year
                } else {
                    val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC+3"))
                    calendar.timeInMillis = utcTimeMillis
                    calendar[Calendar.DAY_OF_WEEK] != Calendar.SUNDAY &&
                            calendar[Calendar.DAY_OF_WEEK] != Calendar.SATURDAY
                }
            }

            override fun isSelectableYear(year: Int): Boolean {
                return true
            }
        },
        initialSelectedDateMillis = selectedDay.collectAsState().value.dayToMillis(),
        initialDisplayedMonthMillis = selectedDay.collectAsState().value.dayToMillis()
    )
    val chooseDate = dateState.selectedDateMillis?.let {//выбранная дата
        DateUtils().convertMillisToLocalDate(it)
    }

    if (Screen.Diary.dialog.value || Screen.Stats.dialog.value) {
        DatePickerDialog(
            onDismissRequest = {
                Screen.Diary.dialog.value = false
                Screen.Stats.dialog.value = false
            },
            confirmButton = {
                Button(
                    onClick = {
                        Screen.Diary.dialog.value = false
                        Screen.Stats.dialog.value = false
                        if (chooseDate != null) {
                            val thisMouth = Instant.now().atZone(ZoneId.of("UTC+3"))
                                .toLocalDate().month == chooseDate.month
                            if (thisMouth) {
                                onSelectedDay(
                                    (chooseDate.dayOfMonth + chooseDate.minusMonths(1).month.length(
                                        chooseDate.isLeapYear
                                    )) - 1
                                )
                            } else onSelectedDay(chooseDate.dayOfMonth - 1)
                        }
                    }
                ) {
                    Text(text = stringResource(R.string.add_sleep_btn))
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        Screen.Diary.dialog.value = false
                        Screen.Stats.dialog.value = false
                    }
                ) {
                    Text(text = stringResource(R.string.cancel_btn))
                }
            }
        ) {
            DatePicker(
                state = dateState,
                showModeToggle = true
            )
        }
    }
}

//@RequiresApi(Build.VERSION_CODES.O)
//@Preview(showBackground = true,
//    showSystemUi = true)
//@Composable
//fun ManufacturedDatePreview() {
//    DateDialogsSampleTheme {
//        ManufacturedDate()
//    }
//}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun ManufacturedDateWithDialogPreview() {
    MyHealthTheme {
        // DatePickerWithDialog()
    }
}