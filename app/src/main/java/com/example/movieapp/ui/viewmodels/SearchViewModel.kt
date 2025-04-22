package com.example.movieapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.db.MovieEntity
import com.example.movieapp.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val repo: MovieRepository
) : ViewModel() {
    private val _searchResults = MutableStateFlow<List<MovieEntity>>(emptyList())
    val searchResults: StateFlow<List<MovieEntity>> = _searchResults

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun onQueryChanged(query: String) {
        if (query.isEmpty()) {
            _searchResults.value = emptyList()
            return
        }
        viewModelScope.launch {

            try {
                _isLoading.value = true
                val resp = repo.search(query, 1)
                val list = resp.results.map { it.toEntity(resp.page) }
                _searchResults.value = list
                _isLoading.value = false
            }
            catch (e: Exception) {
                try {
                    val list = repo.searchMoviesDB(query)
                    _searchResults.value = list
                    _isLoading.value = false
                } catch (e: Exception) {
                    _isLoading.value = false
                    e.printStackTrace()
                }
            }

        }
    }
}