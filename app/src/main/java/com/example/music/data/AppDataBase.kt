package com.example.music.data

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [Gender::class], version = 1)
abstract class AppDataBase : RoomDatabase(){
    abstract fun genderDao(): GenderDao
}