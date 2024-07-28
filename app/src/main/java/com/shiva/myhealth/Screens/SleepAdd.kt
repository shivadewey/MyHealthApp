package com.shiva.myhealth.Screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.shiva.myhealth.R
import com.shiva.myhealth.data.Sleep
import com.shiva.myhealth.models.DiaryViewModel
import com.shiva.myhealth.models.SleepAddViewModel
import com.shiva.myhealth.ui.theme.MyHealthTheme
import com.shiva.myhealth.utility.parseFloat
import com.shiva.myhealth.utility.parseInt
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox

@Composable
fun SleepAdd(
    modifier: Modifier,
    modelDiary: DiaryViewModel = hiltViewModel(),
    model: SleepAddViewModel = hiltViewModel()
) {
    val sleepList = remember { model.sleepList }
    val editedSleep by remember { model.editedSleep }

    LaunchedEffect(key1= Unit){
        model.getSleepList(modelDiary)
    }


    if (model.sleepAddDialog) {
        SleepDetailDialog(
            showDialog = model::sleepAddDialogShow,
            sleep = editedSleep,
            onAcceptSleep = model::addSleep,
            editSleep = model::editSleep
        )
    }
    if (model.sleepEditDialog) {
        SleepDetailDialog(
            showDialog = model::sleepEditDialogShow,
            sleep = editedSleep,
            onAcceptSleep = model::addSleep,
            editSleep = model::editSleep,
            isEdit = true
        )
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        //TODO выбор времени?
        Row (verticalAlignment = Alignment.CenterVertically){
            Text(
                text = stringResource(R.string.sleep_add),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(end = 8.dp)
            )
            FloatingActionButton(onClick = { model.onAddSleepClick(Sleep())}) {
                Icon(Icons.Default.Add, contentDescription = "Добавить")
            }
        }


        //TODO список выбранных продуктов
        SleepDetailList(sleepList, model::onEditSwipe, model::onDelSwipe)

    }
}


@Composable
fun SleepDetailList(
    sleeps: SnapshotStateList<Sleep>,
    onEditSwipe: (Sleep) -> Unit,
    onDelSwipe: (Sleep) -> Unit
) {
    LazyColumn(
        modifier = Modifier.padding(8.dp).fillMaxWidth().background(
            color = MaterialTheme.colorScheme.secondaryContainer.copy(
                alpha = .50f
            ), shape = RoundedCornerShape(8.dp)
        )
    ) {
        items(sleeps) {

            val delete = SwipeAction(onSwipe = {
                onDelSwipe(it)
            }, icon = {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete sleep",
                    modifier = Modifier.padding(16.dp),
                    tint = Color.White
                )
            }, background = Color.Red.copy(alpha = 0.5f), isUndo = true
            )
            val archive = SwipeAction(onSwipe = {
                onEditSwipe(it)
            }, icon = {
                Icon(
                    Icons.Default.Edit,
                    contentDescription = "edit sleep",
                    modifier = Modifier.padding(16.dp),

                    tint = Color.White

                )
            }, background = Color(0xFF50B384).copy(alpha = 0.7f)
            )
            SwipeableActionsBox(
                modifier = Modifier,
                swipeThreshold = 200.dp,
                startActions = listOf(archive),
                endActions = listOf(delete)
            ) {
                SleepDetailListItem(it)
            }
        }
    }
}

@Composable
fun SleepDetailListItem(sleep: Sleep) {
    Column(
        modifier = Modifier.padding(8.dp).fillMaxWidth().background(
            color = MaterialTheme.colorScheme.secondaryContainer.copy(
                alpha = .50f
            ), shape = RoundedCornerShape(8.dp)
        ), horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Column(
            modifier = Modifier.padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                "${stringResource(R.string.sleep_title)}: ${sleep.hours} ${stringResource(R.string.hours)}",
                Modifier.padding(end = 4.dp),
                style = MaterialTheme.typography.labelLarge,

                )
            Row {
                Text(
                    "${stringResource(R.string.wake_up_title)} ",
                    style = MaterialTheme.typography.labelLarge
                )
                Icon(Icons.Default.Alarm, "Alarm")
                Text(
                    ": ",
                    style = MaterialTheme.typography.labelLarge
                )
                val isAlarmed = if (sleep.isAlarmed) Icons.Default.Done else Icons.Default.Cancel
                val color = if (sleep.isAlarmed) Color.Green.copy(0.75f) else Color.Red.copy(0.75f)
                Icon(isAlarmed, "Alarmed", tint = color)
            }

        }
        OutlinedTextField(sleep.description,
            { sleep.description = it },
            Modifier.fillMaxWidth().padding(start = 8.dp, bottom = 8.dp, end = 8.dp),
            enabled = false,
            label = { Text(stringResource(R.string.description)) })
    }
}

@Composable
fun SleepDetailDialog(
    showDialog: (Boolean) -> Unit,
    sleep: Sleep = Sleep(),
    onAcceptSleep: (Sleep) -> Unit,
    editSleep: (Sleep) -> Unit,
    isEdit: Boolean = false
) {

    val hours = remember { mutableIntStateOf(sleep.hours.toInt()) }
    val minutes = remember { mutableIntStateOf((sleep.hours.minus(hours.intValue) * 60).toInt()) }
    val summeryHours = remember { mutableFloatStateOf(hours.intValue + (minutes.intValue / 60).toFloat()) }
    val description = remember { mutableStateOf(sleep.description) }
    val isAlarmed = remember { mutableStateOf(sleep.isAlarmed) }
    val toastShow = remember { mutableStateOf(false) }
    Dialog(
        onDismissRequest = { showDialog(false) }, DialogProperties(
            dismissOnBackPress = false, dismissOnClickOutside = true
        )
    ) {
        if (toastShow.value) {
            Toast.makeText(LocalContext.current, R.string.toast_add, Toast.LENGTH_LONG).show()
            toastShow.value = false
        }
        Surface(
            modifier = Modifier.fillMaxWidth().height(500.dp).padding(8.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier.padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(Icons.Default.Bedtime, "")

                HorizontalDivider(thickness = 4.dp, color = Color.Transparent)

                Row(
                    Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {

                    OutlinedTextField(
                        hours.intValue.toString(),
                        {
                            if (it != "") {
                                hours.intValue = it.parseInt(it)
                                summeryHours.floatValue = (hours.intValue + (minutes.value / 60)).toFloat()
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        label = { Text(stringResource(R.string.hours)) },
                        modifier = Modifier.padding(end = 8.dp).width(80.dp)
                    ) // часы

                    OutlinedTextField(
                        minutes.intValue.toString(),
                        {
                            if (it != "") {
                                minutes.intValue = it.parseInt(it)
                                summeryHours.floatValue = (hours.intValue + (minutes.intValue / 60)).toFloat()
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        label = { Text(stringResource(R.string.minutes)) },
                        modifier = Modifier.width(80.dp)
                    ) //минуты
                }


                OutlinedTextField(summeryHours.floatValue.toString(),
                    { summeryHours.floatValue = it.parseFloat(it) },
                    enabled = false,
                    leadingIcon = { Icon(Icons.Default.Schedule, "") },
                    label = { Text(stringResource(R.string.hours)) }) //общее время

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "${stringResource(R.string.wake_up_title)} ",
                        style = MaterialTheme.typography.labelLarge
                    )
                    Icon(Icons.Default.Alarm, "Alarm")
                    Text(
                        ":",
                        style = MaterialTheme.typography.labelLarge
                    )
                    Checkbox(isAlarmed.value, //проснулся по будильнику
                        { isAlarmed.value = it })
                }


                OutlinedTextField(description.value,
                    { description.value = it },
                    minLines = 2,
                    maxLines = 3,
                    label = { Text(stringResource(R.string.description)) }) //описание


                Button({
                    if (!isEdit && (hours.intValue!=0 || minutes.intValue!=0))
                        onAcceptSleep(
                            Sleep(
                                summeryHours.floatValue,
                                description.value,
                                isAlarmed.value
                            )
                        )
                    else if (isEdit && (hours.intValue!=0 || minutes.intValue!=0)) {
                        editSleep(
                            Sleep(
                                summeryHours.floatValue,
                                description.value,
                                isAlarmed.value
                            )
                        )
                    } else toastShow.value = true
                }, Modifier) {
                    Text(stringResource(R.string.add_sleep_btn))
                }

                Button({ showDialog(false) }, Modifier) {
                    Text(stringResource(R.string.cancel_btn))
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SleepAddPreview() {
    MyHealthTheme {
        //SleepAdd("SleepAdd")
    }
}