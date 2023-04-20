package com.example.music

import android.app.Application
import androidx.room.Room
import com.example.music.data.AppDataBase

class MusicApplication : Application() {

    private lateinit var dataBase: AppDataBase

    override fun onCreate() {
        super.onCreate()

        dataBase = Room.databaseBuilder(
            applicationContext,
            AppDataBase::class.java, "gender-database"
        ).build()
    }

    fun getAppDataBase():AppDataBase{
        return dataBase
    }
}