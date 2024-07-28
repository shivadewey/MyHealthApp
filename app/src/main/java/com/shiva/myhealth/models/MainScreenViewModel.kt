package com.shiva.myhealth.models

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.shiva.myhealth.data.Day
import com.shiva.myhealth.data.Person
import com.shiva.myhealth.room.HealthRoomDb
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.stream.IntStream
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
     val mainDB: HealthRoomDb
) : ViewModel() {

    lateinit var diaryModel: DiaryViewModel
    lateinit var foodAddViewModel: FoodAddViewModel
    lateinit var sleepAddViewModel: SleepAddViewModel
    lateinit var statsViewModel: StatsViewModel
    lateinit var accountViewModel: AccountViewModel

    private val currDay: LocalDate = Instant.now().atZone(ZoneId.of("UTC+3")).toLocalDate()
    var days = mutableStateListOf<Day>()
        get() {
            field.map {
                it.goalCalories = person.value?.caloriesGoal?.toInt() ?: 1000
                it.goalSleep = person.value?.sleepGoal ?: 8f
            }
            return field
        }
    var selectedDayIndex = MutableStateFlow(getListSize() - 1)
    var selectedDay = MutableStateFlow(
        Day(
            date = currDay,
            foodCount = 1, //получать с базы
            totalCalories = 0, // рассчитывать и получать с бд
            //рассчитывать 2 параметра еще
        )
    )
    lateinit var person: LiveData<Person>

    private fun getPerson() = viewModelScope.launch {
        person = liveData {
            mainDB.personDao.getPerson()
        }
    }

    init {
        getPerson()
        initDayList(getListSize()).toCollection(days)
    }

    var inSystem = MutableStateFlow(false)
        set(value) {
            days.clear()
            initDayList(getListSize()).toCollection(days)
            field = value
        }


    init {
        initDayList(getListSize()).toCollection(days)
    }

    fun initiate(
        diaryModel: DiaryViewModel,
        foodAddViewModel: FoodAddViewModel,
        sleepAddViewModel: SleepAddViewModel,
        statsViewModel: StatsViewModel,
        accountViewModel: AccountViewModel,
    ) {
        this.diaryModel = diaryModel
        this.foodAddViewModel = foodAddViewModel
        this.sleepAddViewModel = sleepAddViewModel
        this.statsViewModel = statsViewModel
        this.accountViewModel = accountViewModel
        //получать, в системе ли
    }

    fun updatePersonDate(person: Person) {
        this.person = MutableLiveData(person)
        inSystem = MutableStateFlow(true)
    }


    private fun getListSize(): Int {
        return currDay.dayOfMonth + currDay.minusMonths(1).month.length(currDay.isLeapYear)
    }

    private fun initDayList(count: Int): List<Day> {
        val list: MutableList<Day> = emptyList<Day>().toMutableList()
        for (i in IntStream.range(1, count + 1)) {
            val day =
                if (i <= currDay.minusMonths(1).month.length(currDay.isLeapYear)) currDay.minusMonths(
                    1
                ).withDayOfMonth(i)
                else currDay.withDayOfMonth(i - currDay.minusMonths(1).month.length(currDay.isLeapYear))

            list.add(
                Day(
                    date = day,
                    foodCount = 1,
                    totalCalories = 0,
                    goalSleep = person.value?.sleepGoal ?: 8f,
                    goalCalories = person.value?.caloriesGoal?.toInt() ?: 1000
                )
            )
        }
        list.forEach { it.updateAllCount() }

        selectedDay = MutableStateFlow(list.last())
        return list
    }

    private fun updateDataInDayList() {
        days[selectedDayIndex.value].breakfast = selectedDay.value.breakfast
        days[selectedDayIndex.value].lunch = selectedDay.value.lunch
        days[selectedDayIndex.value].dinner = selectedDay.value.dinner
        days[selectedDayIndex.value].bedTime = selectedDay.value.bedTime
    }

    fun selected(index: Int) {
        updateDataInDayList()
        selectedDayIndex.value = index
        selectedDay.value = days[index]
        selectedDay.value.breakfast = days[index].breakfast
        selectedDay.value.lunch = days[index].lunch
        selectedDay.value.dinner = days[index].dinner
        selectedDay.value.bedTime = days[index].bedTime
        selectedDay.value.updateAllCount()
    }
}