package com.example.movieapp.network.model

import org.junit.Assert.*
import org.junit.Test

class GenresTest {

    @Test
    fun `test Genres data class creation`() {
        // Given
        val genre = Genres(id = 1, name = "Action")

        // Then
        assertEquals(1, genre.id)
        assertEquals("Action", genre.name)
    }

    @Test
    fun `test Genres data class equality`() {
        // Given
        val genre1 = Genres(id = 1, name = "Action")
        val genre2 = Genres(id = 1, name = "Action")

        // Then
        assertEquals(genre1, genre2) // Verify that both are equal
    }

    @Test
    fun `test Genres data class hash code`() {
        // Given
        val genre1 = Genres(id = 1, name = "Action")
        val genre2 = Genres(id = 1, name = "Action")

        // Then
        assertEquals(genre1.hashCode(), genre2.hashCode()) // Verify that both have the same hashCode
    }

    @Test
    fun `test Genres data class with different data`() {
        // Given
        val genre1 = Genres(id = 1, name = "Action")
        val genre2 = Genres(id = 2, name = "Comedy")

        // Then
        assertNotEquals(genre1, genre2) // Verify that both are not equal due to different id and name
    }
}
