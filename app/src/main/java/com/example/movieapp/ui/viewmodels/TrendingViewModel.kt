package com.example.movieapp.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.map
import com.example.movieapp.db.MovieEntity
import com.example.movieapp.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "TrendingViewModel"

class TrendingViewModel(
    private val repo: MovieRepository
) : ViewModel() {

    private val _movies = MutableStateFlow<List<MovieEntity>>(emptyList())
    val movies: StateFlow<List<MovieEntity>> = _movies

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
//        Log.d(TAG, "init: TrendingViewModel ${logDatabaseContent()}")
        loadTrendingPage(1)

    }

    private fun loadTrendingPage(page: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val list = repo.loadAndCacheTrending(page)
                Log.d(TAG, "loadTrendingPage: fetched ${list.size} items for page $page")
                _movies.value = list
            } catch (e: Exception) {
                try {
                    val list = repo.getMoviePagingPage(page)
                    Log.d(TAG, "loadTrendingPage: fetched ${list.size} items from DB")
                    _movies.value = list
                } catch (e: Exception) {
                    Log.e(TAG, "loadTrendingPage: error fetching page $page", e)
                }
            } finally {
                _isLoading.value = false
            }
        }
    }



    private fun logDatabaseContent() {
        viewModelScope.launch {
            try {
                val movies = repo.getAllMoviesFromDb() // Gọi hàm từ repository
                Log.d(TAG, "Database content: ${movies.joinToString { it.toString() }}")
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching data from database", e)
            }
        }
    }
}
