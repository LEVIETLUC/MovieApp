package com.example.movieapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MovieDetailDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(detail: MovieDetailEntity)

    @Query("SELECT * FROM movie_details WHERE id = :id")
    fun getById(id: Int): MovieDetailEntity?
}