package com.example.movieapp.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.movieapp.db.MovieEntity
import com.example.movieapp.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

private const val TAG = "TrendingViewModel"

class TrendingViewModel(
    private val repo: MovieRepository
) : ViewModel() {
    private val _movies = MutableStateFlow<List<MovieEntity>>(emptyList())
    val movies: StateFlow<List<MovieEntity>> = _movies

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private var currentPage = 0
    private var endReached = false

    init {
        loadNextPage()

    }

    fun loadNextPage() {
        if (_isLoading.value || endReached) return
        val nextPage = currentPage + 1
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val newItems = repo.loadAndCacheTrending(nextPage)
                Log.d(TAG, "loadNextPage: fetched ${newItems.size} items for page $nextPage")
                if (newItems.isEmpty()) {
                    endReached = true
                } else {
                    currentPage = nextPage
                    _movies.value = _movies.value + newItems
                }
            } catch (e: Exception) {
                try {
                    val newItems = repo.getMoviePagingPage(nextPage)
                    Log.d(TAG, "loadNextPage: fetched ${newItems.size} items from DB")
                    if (newItems.isEmpty()) {
                        endReached = true
                    } else {
                        currentPage = nextPage
                        _movies.value = _movies.value + newItems
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "loadNextPage: error fetching page $nextPage", e)
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
