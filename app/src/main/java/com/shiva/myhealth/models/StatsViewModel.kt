package com.shiva.myhealth.models

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.shiva.myhealth.R
import com.shiva.myhealth.data.Day
import com.shiva.myhealth.data.Person
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class StatsViewModel @Inject constructor() : ViewModel() {
     lateinit var context : Context
    lateinit var person: LiveData<Person>
    var selectedDay = mutableStateOf(Day(Instant.now().atZone(ZoneId.of("UTC+3")).toLocalDate()))
    var days = mutableStateListOf<Day>()
    var daysFiltered = mutableStateListOf<Day>()
    var selectedDayIndex = MutableStateFlow(getListSize() - 1)
    var dateSelectionBtn = mutableStateMapOf(
        DateSelection.Day to true,
        DateSelection.Week to false,
        DateSelection.Mounth to false,
    )
    var strike = mutableIntStateOf(0)
    var bestStrike = mutableIntStateOf(0)

    var avrCount = mutableStateListOf<Any>()
    var avrCalories = mutableStateListOf<Any>()

    val progressCalories =
        mutableFloatStateOf(selectedDay.value.totalCalories.toFloat() / selectedDay.value.goalCalories)
    val progressSleep =
        mutableFloatStateOf(selectedDay.value.totalSleep / selectedDay.value.goalSleep)
    var onSelectedDay: (Int) -> Unit = {}
    fun getDayData(days: List<Day>, selectedDayIndex: State<Int>, onSelect: (Int) -> Unit, context: Context,person: LiveData<Person>) {
        this.context=context
        this.days.clear()
        this.avrCalories.clear()
        this.avrCount.clear()
        days.toCollection(this.days)
        this.strike.intValue = 0
        this.bestStrike.intValue = 0
        this.selectedDayIndex.value = selectedDayIndex.value
        this.selectedDay.value = days[selectedDayIndex.value]
        this.onSelectedDay = onSelect
        selectedDay.value.updateAllCount()
        updateProgress()
        this.person=person
    }

    fun onDateSelection(sel: DateSelection) {
        when (sel) {
            DateSelection.Day -> {
                dateSelectionBtn[DateSelection.Day] = true
                dateSelectionBtn[DateSelection.Week] = false
                dateSelectionBtn[DateSelection.Mounth] = false
                daysFiltered.clear()
                daysFiltered.add(selectedDay.value)
            }

            DateSelection.Week -> {
                dateSelectionBtn[DateSelection.Day] = false
                dateSelectionBtn[DateSelection.Week] = true
                dateSelectionBtn[DateSelection.Mounth] = false
                daysFiltered.clear()
                val startIndex =
                    if ((selectedDayIndex.value - 7) >= 0) selectedDayIndex.value - 7 else 0
                daysFiltered.addAll(days.subList(startIndex, selectedDayIndex.value))
            }

            DateSelection.Mounth -> {
                dateSelectionBtn[DateSelection.Day] = false
                dateSelectionBtn[DateSelection.Week] = false
                dateSelectionBtn[DateSelection.Mounth] = true
                daysFiltered.clear()
                val startIndex =
                    if ((selectedDayIndex.value - 30) >= 0) selectedDayIndex.value - 30 else 0
                daysFiltered.addAll(days.subList(startIndex, selectedDayIndex.value))
            }
        }
        updateProgress()
    }

    private fun calculateAverrageParams() {
        val breakfastProductCount =
            if (days.count { it.breakfast.products.isNotEmpty() } != 0) days.fold(
                0
            ) { a, b -> a + b.breakfast.products.size } / days.count { it.breakfast.products.isNotEmpty() } else context.resources.getString(
                R.string.no_data
            )


        val lunchProductCount =
            if (daysFiltered.count { it.lunch.products.isNotEmpty() } != 0)
                days.fold(0) { a, b -> a + b.lunch.products.size } / days.count { it.lunch.products.isNotEmpty() } else context.resources.getString(
                R.string.no_data
            )

        val dinnerProductCount =
            if (days.count { it.dinner.products.isNotEmpty() } != 0) days.fold(0) { a, b -> a + b.dinner.products.size } / days.count { it.dinner.products.isNotEmpty() } else context.resources.getString(
                R.string.no_data
            )


        val breakfastCalories =
            if (days.count { it.breakfast.products.isNotEmpty() } != 0) days.fold(0f) { a, b -> a + b.breakfast.getFoodTimeCalories() } / days.count { it.breakfast.products.isNotEmpty() } else context.resources.getString(
                R.string.no_data
            )


        val lunchCalories =
            if (days.count { it.lunch.products.isNotEmpty() } != 0) days.fold(0f) { a, b -> a + b.lunch.getFoodTimeCalories() } / days.count { it.lunch.products.isNotEmpty() } else context.resources.getString(
                R.string.no_data
            )


        val dinnerCalories =
            if (days.count { it.dinner.products.isNotEmpty() } != 0) days.fold(0f) { a, b -> a + b.dinner.getFoodTimeCalories() } / days.count { it.dinner.products.isNotEmpty() } else context.resources.getString(
                R.string.no_data

            )

        listOf(breakfastProductCount, lunchProductCount, dinnerProductCount).toCollection(avrCount)
        listOf(breakfastCalories, lunchCalories, dinnerCalories).toCollection(avrCalories)
    }

    private fun updateProgress() {
        progressCalories.floatValue = (daysFiltered.fold(0) { a, b -> a + b.totalCalories }
            .toFloat() / daysFiltered.size) / selectedDay.value.goalCalories
        progressSleep.floatValue = (daysFiltered.fold(0f) { a, b -> a + b.totalSleep }
            .toFloat() / daysFiltered.size) / selectedDay.value.goalSleep
        val maxVal = emptyList<Int>().toMutableList()
        daysFiltered.forEach {
            if (it.isStrike) {
                strike.intValue++
            } else {
                if (!maxVal.contains(strike.intValue)&&strike.intValue!=0)
                    maxVal.add(strike.intValue)
                strike.intValue = 0
            }
        }
        bestStrike.intValue = if (maxVal.isNotEmpty()) maxVal.max() else strike.intValue
        //calculateAverrageParams()
    }

    private fun getListSize(): Int {
        val currDay: LocalDate = Instant.now().atZone(ZoneId.of("UTC+3")).toLocalDate()
        return currDay.dayOfMonth + currDay.minusMonths(1).month.length(currDay.isLeapYear)
    }

    enum class DateSelection {
        Day,
        Week,
        Mounth
    }
}