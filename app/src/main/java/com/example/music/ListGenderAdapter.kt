package com.example.music

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView


class ListGenderAdapter(
    private val openMusicDetail:(gender: Gender) -> Unit
 ) : ListAdapter<Gender, ListGenderViewHolder>(ListGenderAdapter){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListGenderViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_gender, parent, false)
        return ListGenderViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListGenderViewHolder, position: Int) {
        val gender = getItem(position)
        holder.bind(gender, openMusicDetail)
    }

    companion object : DiffUtil.ItemCallback<Gender>(){
        override fun areItemsTheSame(oldItem: Gender, newItem: Gender): Boolean {
            return oldItem == newItem
        }
        override fun areContentsTheSame(oldItem: Gender, newItem: Gender): Boolean {
            return oldItem.artista == newItem.artista &&
                    oldItem.musica == newItem.musica
        }

    }
}

class ListGenderViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    private var tvArtista = view.findViewById<TextView>(R.id.tv_artista)
    private var tvMusic = view.findViewById<TextView>(R.id.tv_musica)

    fun bind(gender: Gender, openMusicDetail:(gender: Gender) -> Unit){
        tvArtista.text = gender.artista
        tvMusic.text = gender.musica

        view.setOnClickListener {
            openMusicDetail.invoke(gender)
        }
    }
}