package com.shiva.myhealth.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.shiva.myhealth.data.Day

@Dao
interface DayDao {
    @Query("SELECT * FROM days")
    fun getDays(): LiveData<List<Day>>
    @Insert
    fun addDay(day: Day)

    @Update
    fun updDay(day: Day)
}