package com.example.music

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update


@Dao
interface GenderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(gender: Gender)

    @Query("Select * from gender")
    fun getAll(): List<Gender>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(gender: Gender)

    //deletar TUDO
    @Query("delete from gender")
    fun deleteAll()

    //deletar por ID
    @Query("delete from gender where id= :id")
    fun deleteById(id: Int)
}