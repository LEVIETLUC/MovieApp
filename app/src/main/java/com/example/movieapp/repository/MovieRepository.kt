package com.example.movieapp.repository


import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.map
import com.example.movieapp.db.MovieDao
import com.example.movieapp.db.MovieDetailDao
import com.example.movieapp.db.MovieEntity
import com.example.movieapp.db.MovieDetailEntity
import com.example.movieapp.db.TmdbDatabase
import com.example.movieapp.network.TmdbService
import com.example.movieapp.network.model.MovieResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MovieRepository(
    private val service: TmdbService,
    private val movieDao: MovieDao,
    private val detailDao: MovieDetailDao,
    private val db: TmdbDatabase
) {

    @OptIn(ExperimentalPagingApi::class)


    // Load & cache trending pages
    suspend fun loadAndCacheTrending(page: Int): List<MovieEntity> {
        val resp = service.getTrending(page)
        Log.d(
            "MovieRepository",
            "loadAndCacheTrending: Fetched ${resp.results.size} movies for page $page"
        )
        Log.d("MovieRepository", "loadAndCacheTrending: raw response = $resp")
        val entities = resp.results.map { it.toEntity(page) }
        withContext(Dispatchers.IO) {
            movieDao.insertAll(entities)
            entities.forEach {
                launch {
                    try {
                        val movieDetail = service.getMovieDetails(it.id).toEntity()
                        detailDao.insert(movieDetail)
                    } catch (e: Exception) {
                        Log.e("MovieRepository", "Error fetching details for movie id ${it.id}", e)
                    }
                }
            }
            Log.e(
                "MovieRepository",
                "loadAndCacheTrending: inserted ${entities.size} movies for page $page"
            )
        }
        return entities
    }

    suspend fun search(query: String, page: Int): MovieResponse {
        return service.searchMovies(query, page)
    }

    suspend fun getDetail(id: Int): MovieDetailEntity {
        try {
            val resp = service.getMovieDetails(id)
            Log.d("MovieRepository", "getDetail: Fetched movie details for id $id")
            Log.d("MovieRepository", "getDetail: raw response = $resp")
            val entity = resp.toEntity()
            return entity
        } catch (e: Exception) {
            try {
                val entity = getMovieDetail(id)
                Log.d("MovieRepository", "getDetail: Fetched movie details for id $id from DB")
                Log.d("MovieRepository", "getDetail: raw response = $entity")
                if (entity != null) {
                    return entity
                } else {
                    throw Exception("Movie details not found in database for id $id")
                }
            } catch (e: Exception) {
                Log.e("MovieRepository", "getDetail: Error fetching movie details for id $id", e)
                throw e
            }
        }

    }

    suspend fun getMovieDetail(id: Int): MovieDetailEntity? {
        return withContext(Dispatchers.IO) {
            detailDao.getById(id)
        }
    }

    suspend fun getAllMoviesFromDb(): List<MovieEntity> {
        return withContext(Dispatchers.IO) {
            movieDao.getAllMovies()
        }
    }

    suspend fun searchMoviesDB(query: String): List<MovieEntity> {
        return withContext(Dispatchers.IO) {
            movieDao.searchMovies(query)
        }
    }

    suspend fun getMoviePagingPage(page: Int): List<MovieEntity> {
        return withContext(Dispatchers.IO) {
            val result = movieDao.pagingSource(page).load(
                PagingSource.LoadParams.Refresh(
                    key = page,
                    loadSize = 20,
                    placeholdersEnabled = false
                )
            )
            if (result is PagingSource.LoadResult.Page) {
                result.data
            } else {
                emptyList()
            }
        }
    }

}