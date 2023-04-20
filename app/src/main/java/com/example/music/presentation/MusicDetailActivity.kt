package com.example.music.presentation

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.example.music.R
import com.example.music.data.Gender
import com.google.android.material.snackbar.Snackbar


class MusicDetailActivity : AppCompatActivity() {

    private var gender: Gender? = null
    private lateinit var btnDone: Button

    companion object{
        private const val MUSIC_DETAIL_EXTRA = "music.extra.detail"

        fun start(context: Context, music: Gender?): Intent {
            val intent = Intent(context, MusicDetailActivity::class.java)
                .apply{
                    putExtra(MUSIC_DETAIL_EXTRA, music)
                }
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_detail)

        gender = intent.getSerializableExtra(MUSIC_DETAIL_EXTRA) as Gender?

        //tvMusic = findViewById(R.id.tv_music_detail)
        //tvMusic.text = music?.artista ?: "Adicione uma música"

        val edtArtista: EditText = findViewById(R.id.edt_band_detail)
        val edtMusic: EditText = findViewById(R.id.edt_music_detail)
        btnDone = findViewById(R.id.btn_done)

        if (gender != null){
            edtArtista.setText(gender!!.artista)
            edtMusic.setText(gender!!.musica)
        }

        btnDone.setOnClickListener {
            val artista = edtArtista.text.toString()
            val music = edtMusic.text.toString()

            if(artista.isNotEmpty() && music.isNotEmpty()){
                if(gender == null){
                    addOrupdateMusic(0,artista, music, ActionType.CREATE)
                } else {
                    addOrupdateMusic(gender!!.id,artista,music, ActionType.UPDATE)
                }
            }else{
                showMessage(it,"Fields are required")
            }
        }

    }

    private fun addOrupdateMusic(
        id: Int,
        artista: String,
        music:String,
        actionType: ActionType
    ){
        val music = Gender(id,artista,music)
        returnAction(music, actionType)
    }

    private fun returnAction(gender: Gender, actionType: ActionType){
        val intent = Intent()
            .apply {
                val musicAction = MusicAction(gender,actionType.name)
                putExtra(MUSIC_ACTION_EXTRA,musicAction)
            }
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_music -> {

                if(gender != null){
                    returnAction(gender!!, ActionType.DELETE)
                }else{
                    showMessage(btnDone,"Music not found")
                }

                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

    private fun showMessage(view:View, message:String) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
            .setAction("Action", null)
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_music_detail, menu)
        return true
    }
}
