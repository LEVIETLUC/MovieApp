package com.example.movieapp.network.model

import android.R
import com.example.movieapp.db.MovieDetailEntity
import com.squareup.moshi.Json

data class MovieDetailResponse(
    val id: Int,
    val title: String,
    val overview: String,
    @Json(name = "poster_path") val posterPath: String?,
    @Json(name = "backdrop_path") val backdropPath: String?,
    @Json(name = "release_date") val releaseDate: String?,
    @Json(name = "vote_average") val voteAverage: Double,
    @Json(name = "vote_count") val voteCount: Int,
    val runtime: Int?,
    val genres: List<Genres>?,
    val tagline: String?,
    @Json(name = "origin_country") val country: List<String>?,
    @Json(name = "original_language") val languages: String?,
    @Json(name = "production_companies") val productionCompanies: List<ProductionCompanies>?,
    val homepage: String?

) {
    fun toEntity() = MovieDetailEntity(
        id = id,
        title = title,
        overview = overview,
        posterPath = posterPath ?: "",
        backdropPath = backdropPath ?: "",
        releaseDate = releaseDate ?: "",
        voteAverage = voteAverage,
        voteCount = voteCount,
        runtime = runtime,
        genres = genres,
        tagline = tagline,
        country = country,
        languages = languages,
        productionCompanies = productionCompanies,
        homepage = homepage ?: ""

    )
}