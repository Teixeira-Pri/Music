package com.example.music.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Gender(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var artista: String,
    var musica:String
) : Serializable
