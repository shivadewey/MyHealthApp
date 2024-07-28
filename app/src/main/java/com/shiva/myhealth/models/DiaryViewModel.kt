package com.shiva.myhealth.models

import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.shiva.myhealth.Screens.Screen
import com.shiva.myhealth.data.Day
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.stream.IntStream.range
import javax.inject.Inject

@HiltViewModel
class DiaryViewModel @Inject constructor() : ViewModel() {

    lateinit var navHostController: NavHostController
    private val currDay: LocalDate = Instant.now().atZone(ZoneId.of("UTC+3")).toLocalDate()
    val days: MutableStateFlow<List<Day>> = MutableStateFlow(initDayList(getListSize()))
    var selectedDayIndex = MutableStateFlow(getListSize() - 1)
    var selectedDay = MutableStateFlow(
        Day(
            date = currDay,
            foodCount = 1, //получать с базы
            totalCalories = 0, // рассчитывать и получать с бд
            //рассчитывать 2 параметра еще
        )
    )
    var selectedEatTimeName = MutableStateFlow("")

    var onSelectedDay :(Int)->Unit = {}
    fun getDayData(days: List<Day>, selectedDayIndex: State<Int>, onSelect:(Int) -> Unit){
        this.days.value=days
        this.selectedDayIndex.value=selectedDayIndex.value
        this.selectedDay.value = days[selectedDayIndex.value]
        this.onSelectedDay=onSelect
        selectedDay.value.updateAllCount()
    }


    fun onAddFoodBtnClick(foodTimeName: String) {
        navHostController.navigate(Screen.FoodAdd.route + "/${foodTimeName}")
        selectedEatTimeName.value = foodTimeName //для последующего обновления списка продуктов
    }

    fun onSleepAddBtnClick() {
        navHostController.navigate(Screen.SleepAdd.route)
    }

    private fun updateDataInDayList() {
        days.value[selectedDayIndex.value].breakfast = selectedDay.value.breakfast
        days.value[selectedDayIndex.value].lunch = selectedDay.value.lunch
        days.value[selectedDayIndex.value].dinner = selectedDay.value.dinner
        days.value[selectedDayIndex.value].bedTime = selectedDay.value.bedTime
    }

    private fun getListSize(): Int {
        return currDay.dayOfMonth + currDay.minusMonths(1).month.length(currDay.isLeapYear)
    }

    private fun initDayList(count: Int): List<Day> {
        val list: MutableList<Day> = emptyList<Day>().toMutableList()
        for (i in range(1, count + 1)) {
            val thisMouth = i > currDay.minusMonths(1).month.length(currDay.isLeapYear)
            val day =
                if (i <= currDay.minusMonths(1).month.length(currDay.isLeapYear)) currDay.minusMonths(
                    1
                ).withDayOfMonth(i)
                else currDay.withDayOfMonth(i - currDay.minusMonths(1).month.length(currDay.isLeapYear))

            list.add(
                Day(
                    date = day,
                    foodCount = 2, //получать с базы
                    totalCalories = 0, // рассчитывать и получать с бд
                    //рассчитывать 2 параметра еще
                    goalSleep = 10f
                )
            )
        }
        list.forEach { it.updateAllCount() }

        selectedDay = MutableStateFlow(list.last())
        return list
    }


}

