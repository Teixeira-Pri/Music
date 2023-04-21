package com.example.music.presentation

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
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.music.MusicApplication
import com.example.music.R
import com.example.music.data.AppDataBase
import com.example.music.data.Gender
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

    private val viewModel: MusicListViewModel by lazy {
        MusicListViewModel.create(application)
    }

    private val startForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK){
            //pegando o resultado
            val data = result.data
            val musicAction = data?.getSerializableExtra(MUSIC_ACTION_EXTRA) as MusicAction
            viewModel.execute(musicAction)

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.music_list_activity)


        ctnContent = findViewById(R.id.ctn_content)


        val rvPlay: RecyclerView = findViewById(R.id.rv_play)
        rvPlay.adapter = adapter

        val fab = findViewById<FloatingActionButton>(R.id.fab_add)
        fab.setOnClickListener {
            openMusicDetail()
        }
    }

    override fun onStart() {
        super.onStart()

        listFromDataBase()
    }

    private fun deleteAll() {
        val musicAction = MusicAction(null,ActionType.DELETE_ALL.name)
        viewModel.execute(musicAction)
    }

    private fun listFromDataBase(){

            val listObserver = Observer<List<Gender>>{ listGender ->
                if(listGender.isEmpty()){
                    ctnContent.visibility = View.VISIBLE
                } else {
                    ctnContent.visibility = View.GONE
                }
                adapter.submitList(listGender)
            }
          viewModel.musicLiveData.observe(this@MainActivity,listObserver)
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
    DELETE_ALL,
    UPDATE,
    CREATE
}

data class MusicAction(
    val music: Gender?,
    val actionType: String
) : Serializable

const val MUSIC_ACTION_EXTRA = "MUSIC_ACTION_EXTRA"