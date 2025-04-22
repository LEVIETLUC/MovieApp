package com.example.movieapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.db.MovieDetailEntity
import com.example.movieapp.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailViewModel(
    private val repo: MovieRepository
) : ViewModel() {
    private val _movieDetail = MutableStateFlow<MovieDetailEntity?>(null)
    val movieDetail: StateFlow<MovieDetailEntity?> = _movieDetail

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading


    fun load(movieId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _movieDetail.value = repo.getDetail(movieId)
            _isLoading.value = false
        }
    }
}