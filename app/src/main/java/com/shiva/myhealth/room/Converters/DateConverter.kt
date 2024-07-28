package com.shiva.myhealth.room.Converters

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class DateConverter {

        @TypeConverter
        fun fromDate(date: LocalDate): String {
            val formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy")
             return date.format(formatter)
        }

        @TypeConverter
        fun toDate(data: String): LocalDate {
            return LocalDate.parse(data, DateTimeFormatter.ofPattern("dd LLLL yyyy"))
        }
    }