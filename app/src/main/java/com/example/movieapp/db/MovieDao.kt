package com.example.movieapp.db

import android.graphics.Movie
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(movies: List<MovieEntity>)

    @Query("SELECT * FROM movies WHERE page = :page")
    fun pagingSource(page: Int): PagingSource<Int, MovieEntity>

    @Query("SELECT * FROM movies ORDER BY page, id")
    fun allMovies(): List<MovieEntity>

    @Query("DELETE FROM movies")
    fun clearAll(): Int

    @Query("SELECT * FROM movies ORDER BY page, id")
    fun pagingSourceAll(): PagingSource<Int, MovieEntity>

    @Query("SELECT * FROM movies")
    fun getAllMovies(): List<MovieEntity>
}