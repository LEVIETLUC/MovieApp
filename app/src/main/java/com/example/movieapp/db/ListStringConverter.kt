package com.example.movieapp.db

import androidx.room.TypeConverter
import com.example.movieapp.network.model.Genres
import com.example.movieapp.network.model.ProductionCompanies
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ListStringConverter {
    private val gson = Gson()
    private val type = object : TypeToken<List<String>>() {}.type
    private val typeMap = object : TypeToken<List<Map<String, String>>>() {}.type

    @TypeConverter
    fun fromList(value: List<String>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toList(value: String): List<String> {
        return gson.fromJson(value, type) ?: emptyList()
    }

    @TypeConverter
    fun fromListMap(value: List<Map<String, String>>): String {
        return gson.toJson(value)
    }
    @TypeConverter
    fun toListMap(value: String): List<Map<String, String>> {
        return gson.fromJson(value, typeMap) ?: emptyList()
    }

    @TypeConverter
    fun fromGenresList(value: List<Genres>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toGenresList(genresString: String?): List<Genres>? {
        val type = object : TypeToken<List<Genres>>() {}.type
        return gson.fromJson(genresString, type)
    }

    @TypeConverter
    fun fromProductionCompaniesList(value: List<ProductionCompanies>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toProductionCompaniesList(productionCompaniesString: String?): List<ProductionCompanies>? {
        val type = object : TypeToken<List<ProductionCompanies>>() {}.type
        return gson.fromJson(productionCompaniesString, type)
    }
}