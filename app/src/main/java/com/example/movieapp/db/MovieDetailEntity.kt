package com.example.movieapp.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.movieapp.network.model.Genres
import com.example.movieapp.network.model.ProductionCompanies

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
    val country: List<String>?,
    val genres: List<Genres>?,
    val languages: String?,
    val productionCompanies: List<ProductionCompanies>?,
    val homepage: String?

)