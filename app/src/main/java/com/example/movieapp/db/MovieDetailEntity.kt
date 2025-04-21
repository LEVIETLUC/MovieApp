package com.example.movieapp.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "movie_details")
@TypeConverters(ListStringConverter::class) // Thêm annotation này
data class MovieDetailEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String?,
    val backdropPath: String?,
    val releaseDate: String?,
    val voteAverage: Double,
    val voteCount: Int,
    val runtime: Int?,
    val tagline: String?,
    val country: String?,
    val genres: List<String>?,
    val languages: List<String>?,
    val productionCompanies: List<Map<String, String>>?,
    val homepage: String?

)