package com.example.music

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.io.Serializable

class MainActivity : AppCompatActivity() {

    private lateinit var ctnContent: LinearLayout
    //adapter
    private val adapter by lazy {
        ListGenderAdapter(::onListItemClicked)
    }

    private val dataBase by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDataBase::class.java, "gender-database"
        ).build()
    }

    private val dao by lazy {
        dataBase.genderDao()
    }

    private val startForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK){
            //pegando o resultado
            val data = result.data
            val musicAction = data?.getSerializableExtra(MUSIC_ACTION_EXTRA) as MusicAction
            val gender:Gender = musicAction.music

            when (musicAction.actionType) {
                ActionType.DELETE.name -> deleteById(gender.id)
                ActionType.CREATE.name -> insertIntoDataBase(gender)
                ActionType.UPDATE.name -> updateIntoDataBase(gender)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listFromDataBase()

        ctnContent = findViewById(R.id.ctn_content)


        val rvPlay: RecyclerView = findViewById(R.id.rv_play)
        rvPlay.adapter = adapter

        val fab = findViewById<FloatingActionButton>(R.id.fab_add)
        fab.setOnClickListener {
            openMusicDetail()
        }
    }

    private fun insertIntoDataBase(gender: Gender) {
        CoroutineScope(IO).launch {
            dao.insert(gender)
            listFromDataBase()
        }
    }

    private fun deleteById(id: Int) {
        CoroutineScope(IO).launch {
            dao.deleteById(id)
            listFromDataBase()
        }
    }

    private fun updateIntoDataBase(gender: Gender) {
        CoroutineScope(IO).launch {
            dao.update(gender)
            listFromDataBase()
        }
    }
    private fun deleteAll() {
        CoroutineScope(IO).launch {
            dao.deleteAll()
            listFromDataBase()
        }
    }

    private fun listFromDataBase(){
        CoroutineScope(IO).launch {
            val myDataBaseList: List<Gender> = dao.getAll()
            adapter.submitList(myDataBaseList)
        }
    }

    private fun showMessage(view: View, message: String){
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
            .setAction("Action", null)
            .show()
    }

    private fun onListItemClicked(gender: Gender){
        openMusicDetail(gender)
    }
    private fun openMusicDetail (gender: Gender? = null){
        val intent = MusicDetailActivity.start(this, gender)
        startForResult.launch(intent)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_music_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_all_music -> {
                //DELETAR todas as tarefas
                deleteAll()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}

enum class ActionType {

    DELETE,
    UPDATE,
    CREATE
}

data class MusicAction(
    val music: Gender,
    val actionType: String
) : Serializable

const val MUSIC_ACTION_EXTRA = "MUSIC_ACTION_EXTRA"