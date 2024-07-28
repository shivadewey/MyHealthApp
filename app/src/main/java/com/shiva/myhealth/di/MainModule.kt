package com.shiva.myhealth.di

import android.app.Application
import androidx.room.Room
import com.shiva.myhealth.room.HealthRoomDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainModule {

    @Provides
    @Singleton
    fun provideMainDb(app: Application): HealthRoomDb {
        return Room.databaseBuilder(
            app,
            HealthRoomDb::class.java,
            "info.db"
        ).build()
    }
}