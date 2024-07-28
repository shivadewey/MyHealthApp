package com.shiva.myhealth.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.shiva.myhealth.room.Converters.DateConverter
import com.shiva.myhealth.room.dao.DayDao
import com.shiva.myhealth.room.dao.PersonDao
import com.shiva.myhealth.data.Day
import com.shiva.myhealth.data.Person

@Database(
    entities = [(Person::class), (Day::class)],
    version = 1,
)
@TypeConverters(DateConverter::class)
abstract class HealthRoomDb : RoomDatabase() {
    abstract val personDao: PersonDao
    abstract val dayDao: DayDao

    // реализуем синглтон
   /* companion object {
        private var INSTANCE: HealthRoomDb? = null
        fun getInstance(context: Context): HealthRoomDb {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        HealthRoomDb::class.java,
                        "usersdb"
                    ).fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }*/
}