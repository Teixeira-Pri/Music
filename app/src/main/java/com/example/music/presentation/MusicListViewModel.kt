package com.example.music.presentation

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.music.MusicApplication
import com.example.music.data.GenderDao

class MusicListViewModel(private val genderDao: GenderDao): ViewModel() {

    companion object{
        fun create (application: Application):MusicListViewModel{
            val dataBaseInstance = (application as MusicApplication).getAppDataBase()
            val dao = dataBaseInstance.genderDao()
            return MusicListViewModel(dao)
        }
    }
}