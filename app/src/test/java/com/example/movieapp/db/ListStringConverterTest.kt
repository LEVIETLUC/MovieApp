package com.example.movieapp.db

import com.example.movieapp.network.model.Genres
import com.example.movieapp.network.model.ProductionCompanies
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ListStringConverterTest {

    private lateinit var converter: ListStringConverter

    @Before
    fun setUp() {
        converter = ListStringConverter()
    }

    @Test
    fun `fromList should convert list of strings to JSON string`() {
        // Given
        val input = listOf("Action", "Drama", "Comedy")

        // When
        val result = converter.fromList(input)

        // Then
        val expected = "[\"Action\",\"Drama\",\"Comedy\"]"
        assertEquals(expected, result)
    }

    @Test
    fun `toList should convert JSON string to list of strings`() {
        // Given
        val jsonString = """["Action", "Comedy", "Drama"]"""

        // When
        val result = converter.toList(jsonString)

        // Then
        val expected = listOf("Action", "Comedy", "Drama")
        assertEquals(expected, result)
    }
    @Test
    fun `toList should return empty list for empty JSON array`() {
        // Given
        val jsonString = "[]"

        // When
        val result = converter.toList(jsonString)

        // Then
        assertEquals(emptyList<String>(), result)
    }

    @Test
    fun `toListMap should convert JSON string to list of maps`() {
        // Given
        val jsonString = """
            [
                {"name":"Studio Ghibli","country":"Japan"},
                {"name":"Pixar","country":"USA"}
            ]
        """.trimIndent()

        // When
        val result = converter.toListMap(jsonString)

        // Then
        val expected = listOf(
            mapOf("name" to "Studio Ghibli", "country" to "Japan"),
            mapOf("name" to "Pixar", "country" to "USA")
        )

        assertEquals(expected, result)
    }

    @Test
    fun `toListMap should return empty list on empty JSON array`() {
        // Given
        val jsonString = "[]"

        // When
        val result = converter.toListMap(jsonString)

        // Then
        assertEquals(emptyList<Map<String, String>>(), result)
    }


    @Test
    fun `fromGenresList should convert genres list to JSON string`() {
        // Given
        val genres = listOf(
            Genres(id = 1, name = "Action"),
            Genres(id = 2, name = "Comedy")
        )

        // When
        val json = converter.fromGenresList(genres)

        // Then
        val expectedJson = """[{"id":1,"name":"Action"},{"id":2,"name":"Comedy"}]"""
        assertEquals(expectedJson, json)
    }

    @Test
    fun `toGenresList should convert JSON string to list of Genres`() {
        // Given
        val genresJson = """[{"id": 1, "name": "Action"}, {"id": 2, "name": "Comedy"}]"""
        val expectedGenresList = listOf(
            Genres(1, "Action"),
            Genres(2, "Comedy")
        )

        // When
        val result = converter.toGenresList(genresJson)

        // Then
        assertEquals(expectedGenresList, result)
    }

    @Test
    fun `fromProductionCompaniesList should convert list of ProductionCompanies to JSON string`() {
        // Given
        val productionCompanies = listOf(
            ProductionCompanies(1, "/path/to/logo1.png", "Company A", "USA"),
            ProductionCompanies(2, "/path/to/logo2.png", "Company B", "UK")
        )
        val expectedJson = """[{"id":1,"logo_path":"/path/to/logo1.png","name":"Company A","origin_country":"USA"},{"id":2,"logo_path":"/path/to/logo2.png","name":"Company B","origin_country":"UK"}]"""

        // When
        val result = converter.fromProductionCompaniesList(productionCompanies)

        // Then
        assertEquals(expectedJson, result)
    }

    @Test
    fun `toProductionCompaniesList should convert JSON string to list of ProductionCompanies`() {
        // Given
        val productionCompaniesJson = """[
            {"id":1,"logo_path":"/path/to/logo1.png","name":"Company A","origin_country":"USA"},{"id":2,"logo_path":"/path/to/logo2.png","name":"Company B","origin_country":"UK"}]"""
        val expectedCompaniesList = listOf(
            ProductionCompanies(1, "/path/to/logo1.png", "Company A", "USA"),
            ProductionCompanies(2, "/path/to/logo2.png", "Company B", "UK")
        )

        // When
        val result = converter.toProductionCompaniesList(productionCompaniesJson)

        // Then
        assertEquals(expectedCompaniesList, result)
    }

}
