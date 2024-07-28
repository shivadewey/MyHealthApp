package com.shiva.myhealth.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "person")
class Person(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "personId")
    val id: Int? = null ,
    val name: String = "",
    val age: Int = 0,
    val sex: String = "Male",
    val weight: Int = 0,
    val heigth: Int = 0,
    var sleepGoal: Float = 8f,
    var caloriesGoal: Float = 0f
) {
    init {
        //Формула расчета калорийности Тома Венуто
        caloriesGoal = if (sex.equals("Male")) (66 + (13.7f * weight) + (5 * heigth) - (6.8f * age))
        else 665 + (9.6f * weight) + (1.8f * heigth) - (4.7f * age)
    }

}