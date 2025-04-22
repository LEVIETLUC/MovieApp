package com.example.movieapp.network.model
import com.squareup.moshi.Json


data class ProductionCompanies(
    val id: Int,
    @Json(name = "logo_path") val logoPath: String?,
    val name: String,
    @Json(name = "origin_country") val originCountry: String // Map origin_country to originCountry
)
