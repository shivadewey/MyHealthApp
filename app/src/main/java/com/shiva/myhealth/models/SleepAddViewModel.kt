package com.shiva.myhealth.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.shiva.myhealth.data.Sleep
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SleepAddViewModel @Inject constructor() : ViewModel() {


    var sleepAddDialog by mutableStateOf(false)
    var sleepEditDialog by mutableStateOf(false)
    var isChange = false

    var sleepList = mutableStateListOf<Sleep>()
    var editedSleep = mutableStateOf(Sleep())
    private var editSleepIndex by mutableIntStateOf(0)

    fun getSleepList(model: DiaryViewModel) {
            model.selectedDay.value.bedTime.forEach {
            if (!sleepList.contains(it))
                sleepList.add(it)
        }
    }

    fun sleepAddDialogShow(show: Boolean = true) {
        sleepAddDialog = show
    }

    fun sleepEditDialogShow(show: Boolean = true) {
        sleepEditDialog = show
    }

    fun onAddSleepClick(sleep: Sleep) {
        sleepAddDialogShow()
    }

    fun onDelSwipe(sleep: Sleep) {
        sleepList.remove(sleep)
        if (!sleepList.contains(editedSleep.value)){
            editedSleep.value = Sleep()
        }
        isChange=true
    }

    fun onEditSwipe(sleep: Sleep) {
        editSleepIndex = sleepList.indexOf(sleep)
        editedSleep.value = sleep
        sleepEditDialog = true
    }

    fun addSleep(sleep: Sleep) {
        isChange = true
        sleepList.add(sleep)
        sleepAddDialogShow(false)
    }

    fun editSleep(sleep: Sleep) {
        isChange = true
        sleepList[editSleepIndex] = sleep
        sleepEditDialogShow(false)
    }

    fun updateSleepList(model: DiaryViewModel) {
        if (isChange) {
            model.selectedDay.value.bedTime = sleepList
            model.selectedDay.value.updateAllCount()
            isChange = false
        }
        sleepList= mutableStateListOf<Sleep>()
    }

}