package com.example.movieapp.network.model

import com.example.movieapp.network.model.MovieDetailResponse
import com.example.movieapp.db.MovieDetailEntity
import org.junit.Assert.*
import org.junit.Test

class MovieDetailResponseTest {

    @Test
    fun `toEntity maps correctly`() {
        // Arrange: Create a sample MovieDetailResponse
        val response = MovieDetailResponse(
            id = 1,
            title = "Inception",
            overview = "A mind-bending thriller",
            posterPath = "/poster.jpg",
            backdropPath = "/backdrop.jpg",
            releaseDate = "2010-07-16",
            voteAverage = 8.8,
            voteCount = 2000,
            runtime = 148,
            genres = emptyList(),
            tagline = "Your mind is the scene of the crime",
            country = emptyList(),
            languages = "English",
            productionCompanies = emptyList(),
            homepage = "http://www.inceptionmovie.com"
        )

        // Act: Convert to MovieDetailEntity
        val entity = response.toEntity()

        // Assert: Verify the mapping
        assertEquals(response.id, entity.id)
        assertEquals(response.title, entity.title)
        assertEquals(response.overview, entity.overview)
        assertEquals(response.posterPath, entity.posterPath)
        assertEquals(response.backdropPath, entity.backdropPath)
        assertEquals(response.releaseDate, entity.releaseDate)
        assertEquals(response.voteAverage, entity.voteAverage, 0.01)
        assertEquals(response.voteCount, entity.voteCount)
        assertEquals(response.runtime, entity.runtime)
        assertEquals(response.genres, entity.genres)
        assertEquals(response.tagline, entity.tagline)
        assertEquals(response.country, entity.country)
        assertEquals(response.languages, entity.languages)
        assertEquals(response.productionCompanies, entity.productionCompanies)
        assertEquals(response.homepage, entity.homepage)
    }
}
