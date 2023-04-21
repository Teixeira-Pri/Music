package com.example.music.presentation

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.music.MusicApplication
import com.example.music.data.Gender
import com.example.music.data.GenderDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MusicListViewModel(private val genderDao: GenderDao): ViewModel() {

    val musicLiveData: LiveData<List<Gender>> = genderDao.getAll()

    fun execute(musicAction: MusicAction){
        when (musicAction.actionType) {
            ActionType.DELETE.name -> deleteById(musicAction.music!!.id)
            ActionType.CREATE.name -> insertIntoDataBase(musicAction.music!!)
            ActionType.UPDATE.name -> updateIntoDataBase(musicAction.music!!)
            ActionType.DELETE_ALL.name -> deleteAll()
        }
    }

    private fun insertIntoDataBase(gender: Gender) {
        viewModelScope.launch (Dispatchers.IO) {
            genderDao.insert(gender)
        }
    }

    private fun deleteById(id: Int) {
        viewModelScope.launch (Dispatchers.IO) {
            genderDao.deleteById(id)
        }
    }

    private fun updateIntoDataBase(gender: Gender) {
        viewModelScope.launch (Dispatchers.IO) {
            genderDao.update(gender)
        }
    }

    private fun deleteAll() {
        viewModelScope.launch (Dispatchers.IO) {
            genderDao.deleteAll()
        }
    }

    companion object{
        fun create (application: Application):MusicListViewModel{
            val dataBaseInstance = (application as MusicApplication).getAppDataBase()
            val dao = dataBaseInstance.genderDao()
            return MusicListViewModel(dao)
        }
    }
}