package com.example.movieapp.network.model

import com.example.movieapp.db.MovieEntity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovieResponse(
    val page: Int,
    val results: List<MovieDto>,
    @Json(name = "total_pages") val totalPages: Int
)
@JsonClass(generateAdapter = true)
data class MovieDto(
    val id: Int,
    @Json(name = "poster_path") val posterPath: String?,
    val title: String,
    @Json(name = "release_date") val releaseDate: String?,   // make nullable
    @Json(name = "vote_average") val voteAverage: Double
) {
    fun toEntity(page: Int) = MovieEntity(
        id = id,
        title = title,
        posterPath = posterPath.orEmpty(),                   // posterPath ?: ""
        releaseDate = releaseDate.orEmpty(),                 // releaseDate ?: ""
        voteAverage = voteAverage,
        page = page
    )
}