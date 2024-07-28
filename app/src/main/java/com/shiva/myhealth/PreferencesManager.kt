package com.shiva.myhealth

import android.content.Context
import android.content.SharedPreferences
import com.shiva.myhealth.data.Person

class PreferencesManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

    fun saveData(key: String, value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }fun saveData(key: String, value: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun savePerson(person: Person) {
        val editor = sharedPreferences.edit()
        editor.putString("name", person.name)
        editor.putString("sex", person.sex)
        editor.putInt("age", person.age)
        editor.putInt("heigth", person.heigth)
        editor.putFloat("caloriesGoal", person.caloriesGoal)
        editor.putFloat("sleepGoal", person.sleepGoal)
        editor.putInt("weight", person.weight)
        editor.apply()
    }

    fun getData(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    fun getData(key: String, defaultValue: Int): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }

    fun getData(key: String, defaultValue: Float): Float {
        return sharedPreferences.getFloat(key, defaultValue)
    }
    fun getData(key: String, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    fun getPerson(): Person {
        return Person(
            name = getData("name", ""),
            sex = getData("sex", ""),
            age = getData("age", 10),
            heigth = getData("heigth", 0),
            weight = getData("weight", 0),
            sleepGoal = getData("sleepGoal", 8f),
            caloriesGoal = getData("caloriesGoal", 100f),
        )
    }
    fun delData(){
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }


}