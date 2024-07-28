package com.shiva.myhealth.Screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.LocalDining
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shiva.myhealth.R
import com.shiva.myhealth.data.Day
import com.shiva.myhealth.data.Product
import com.shiva.myhealth.models.MainScreenViewModel
import com.shiva.myhealth.models.StatsViewModel
import com.shiva.myhealth.ui.components.ActivityRings
import com.shiva.myhealth.ui.components.DatePickerWithDialog
import com.shiva.myhealth.ui.theme.MyHealthTheme

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun Stats(
    mainModel: MainScreenViewModel,
    model: StatsViewModel = mainModel.statsViewModel
) {
    model.getDayData(
        mainModel.days,
        mainModel.selectedDayIndex.collectAsState(),
        mainModel::selected,
        LocalContext.current,
        mainModel.person
    )
    LaunchedEffect(key1 = Unit) {
        model.onDateSelection(StatsViewModel.DateSelection.Day)
    }

    if (Screen.Stats.dialog.value) DatePickerWithDialog(
        Modifier,
        mainModel.selectedDay,
        mainModel::selected
    )

    LazyColumn(
        modifier = Modifier.padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Row {
                model.dateSelectionBtn[StatsViewModel.DateSelection.Day]?.let {
                    InputChip(
                        it,
                        onClick = { model.onDateSelection(StatsViewModel.DateSelection.Day) },
                        label = { Text(stringResource(R.string.day_btn)) },
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }
                model.dateSelectionBtn[StatsViewModel.DateSelection.Week]?.let {
                    InputChip(
                        it,
                        onClick = { model.onDateSelection(StatsViewModel.DateSelection.Week) },
                        label = { Text(stringResource(R.string.week_btn)) },
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }
                model.dateSelectionBtn[StatsViewModel.DateSelection.Mounth]?.let {
                    InputChip(
                        it,
                        onClick = { model.onDateSelection(StatsViewModel.DateSelection.Mounth) },
                        label = { Text(stringResource(R.string.mounth_btn)) },
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }
            }
        } //date range select
        item {
            CalendarList(
                Modifier,
                model.days,
                model.selectedDayIndex,
                mainModel::selected
            )
        } //calendar select
        item {
            ActivitySection(
                model.strike.intValue.toFloat(),
                model.progressCalories.floatValue,
                model.progressSleep.floatValue,
                model.selectedDay.value,
                model.daysFiltered.size
            )
        } //activity ring section
        item { StrikeSection(model.strike, model.bestStrike) } //series section
        //item { AverrageCaloriesSection(model.avrCount, model.avrCalories) } //averrage section
        item {
            val products = SnapshotStateList<Product>()
            model.daysFiltered.forEach {
                it.breakfast.products.toCollection(products)
                it.lunch.products.toCollection(products)
                it.dinner.products.toCollection(products)
            }
            FoodSection(
                Modifier.fillParentMaxSize(),
                {},
                products
            )
        }//product section
    }
}

@Composable
fun StrikeSection(strike: State<Int>, bestStrike: State<Int>) {

    Column(
        modifier = Modifier.fillMaxWidth().padding(8.dp).background(
            color = MaterialTheme.colorScheme.secondaryContainer.copy(
                alpha = .50f
            ), shape = RoundedCornerShape(8.dp)
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            stringResource(R.string.strike_title),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
        Row {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(stringResource(R.string.strike_title) + ":")
                Text(
                    "${strike.value}",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            VerticalDivider(thickness = 8.dp)
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(stringResource(R.string.best_strike) + ":")
                Text(
                    "${bestStrike.value}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun AverrageCaloriesSection(
    productCount: SnapshotStateList<Any>,
    calories: SnapshotStateList<Any>
) {


    Column(
        modifier = Modifier.fillMaxWidth().padding(8.dp).background(
            color = MaterialTheme.colorScheme.secondaryContainer.copy(
                alpha = .50f
            ), shape = RoundedCornerShape(8.dp)
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            stringResource(R.string.avr_calories_title),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
        Row {
                Column(
                    Modifier.padding(4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(stringResource(R.string.breakfast_title))
                    Text(
                        stringResource(R.string.products) + ": ${productCount[0]}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    HorizontalDivider(Modifier.width(120.dp))
                    Text(
                        "${calories[0]} " + stringResource(R.string.calories),
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Column(
                    Modifier.padding(4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(stringResource(R.string.lunch_title))
                    Text(
                        stringResource(R.string.products) + ": ${productCount[1]}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    HorizontalDivider(Modifier.width(100.dp))
                    Text(
                        "${calories[0]}" + stringResource(R.string.calories),
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Column(
                    Modifier.padding(4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(stringResource(R.string.dinner_title))
                    Text(
                        stringResource(R.string.products) + ": ${productCount[2]}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    HorizontalDivider(Modifier.width(100.dp))
                    Text(
                        "${calories[0]} " + stringResource(R.string.calories),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
        }
    }
}

@Composable
fun ActivitySection(
    comboProgress: Float,
    caloriesProgress: Float,
    sleepProgress: Float,
    day: Day,
    daysCount: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(8.dp).background(
            color = MaterialTheme.colorScheme.secondaryContainer.copy(
                alpha = .50f
            ), shape = RoundedCornerShape(8.dp)
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ActivityRings(
            comboProgress / daysCount,
            caloriesProgress,
            sleepProgress,
            componentSize = 100,
            modifier = Modifier.padding(30.dp).wrapContentSize()
        )
        VerticalDivider(thickness = 8.dp, color = Color.Transparent)
        Column {
            Row {
                Icon(Icons.Default.LocalFireDepartment, "", tint = Color.Red)
                Text(comboProgress.toInt().toString() + " дней", color = Color.Red)
            }

            Row {
                Icon(Icons.Default.LocalDining, "", tint = Color.Green)
                Text(
                    "${day.totalCalories}/${day.goalCalories}  ${stringResource(R.string.calories)}",
                    color = Color.Green
                )
            }

            Row {
                Icon(Icons.Default.Bedtime, "", tint = Color.Blue)
                Text(
                    "${day.totalSleep}/${day.goalSleep} ${stringResource(R.string.hours)}",
                    color = Color.Blue
                )
            }

        }
    }

}

@Preview(showBackground = true)
@Composable
fun StatsPreview() {
    MyHealthTheme {
        //SleepAdd("Stats")
    }
}