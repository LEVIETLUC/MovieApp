package com.example.movieapp.network.model

import org.junit.Assert.*
import org.junit.Test

class MovieResponseTest {

    @Test
    fun `test MovieDto toEntity transformation`() {
        // Given
        val movieDto = MovieDto(
            id = 1,
            posterPath = "/path/to/poster.jpg",
            title = "Movie Title",
            releaseDate = "2025-04-23",
            voteAverage = 8.5
        )
        val page = 1 // Passing page value

        // When
        val movieEntity = movieDto.toEntity(page)

        // Then
        assertEquals(movieDto.id, movieEntity.id)
        assertEquals(movieDto.title, movieEntity.title)
        assertEquals(movieDto.posterPath, movieEntity.posterPath)
        assertEquals(movieDto.releaseDate, movieEntity.releaseDate)
        assertEquals(movieDto.voteAverage, movieEntity.voteAverage, 0.0)
        assertEquals(page, movieEntity.page)
    }

    @Test
    fun `test MovieDto toEntity with null posterPath and releaseDate`() {
        // Given
        val movieDto = MovieDto(
            id = 1,
            posterPath = null, // null value
            title = "Movie Title",
            releaseDate = null, // null value
            voteAverage = 8.5
        )
        val page = 1 // Passing page value

        // When
        val movieEntity = movieDto.toEntity(page)

        // Then
        assertEquals(movieDto.id, movieEntity.id)
        assertEquals(movieDto.title, movieEntity.title)
        assertEquals("", movieEntity.posterPath) // should default to an empty string
        assertEquals("", movieEntity.releaseDate) // should default to an empty string
        assertEquals(movieDto.voteAverage, movieEntity.voteAverage, 0.0)
        assertEquals(page, movieEntity.page)
    }
}
