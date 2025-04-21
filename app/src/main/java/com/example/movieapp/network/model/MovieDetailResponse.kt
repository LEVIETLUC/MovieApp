package com.example.movieapp.network.model

import com.example.movieapp.db.MovieDetailEntity
import com.squareup.moshi.Json

data class MovieDetailResponse(
    val id: Int,
    val title: String,
    val overview: String,
    @Json(name = "poster_path") val posterPath: String?
) {
    fun toEntity() = MovieDetailEntity(
        id = id,
        title = title,
        overview = overview,
        posterPath = posterPath ?: ""
    )
}